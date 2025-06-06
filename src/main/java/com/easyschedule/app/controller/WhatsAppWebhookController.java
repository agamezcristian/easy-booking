package com.easyschedule.app.controller;

import com.easyschedule.app.dto.whatsapp.*;
import com.easyschedule.app.service.WhatsAppService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/webhook")
public class WhatsAppWebhookController {

    private static final Logger logger = LoggerFactory.getLogger(WhatsAppWebhookController.class);

    @Value("${whatsapp.webhook.verify.token}")
    private String verifyToken;
    @Value("${whatsapp.webhook.secret}")
    private String webhookSecret;

    private final WhatsAppService whatsAppService;
    private final ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity<String> verifyWebhook(
            @RequestParam("hub.mode") String mode,
            @RequestParam("hub.challenge") String challenge,
            @RequestParam("hub.verify_token") String token) {

        if ("subscribe".equals(mode) && verifyToken.equals(token)) {
            logger.info("Webhook verificado correctamente");
            return ResponseEntity.ok(challenge);
        }

        logger.warn("Falló la verificación del webhook");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden");
    }

    // Recepción de mensajes (POST)
    @PostMapping
    public ResponseEntity<String> receiveWebhook(
            @RequestBody String payload,
            @RequestHeader("X-Hub-Signature-256") String signature,
            HttpServletRequest request) {

        try {
            logger.info("Recibiendo webhook de WhatsApp");
            logger.debug("Payload: {}", payload);
            // Verificar la firma del webhook
            if (!verifySignature(payload, signature)) {
                logger.warn("Firma del webhook inválida");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            }

            // Parsear el payload
            WhatsAppWebhookPayload webhookPayload = objectMapper.readValue(payload, WhatsAppWebhookPayload.class);

            // Procesar cada entrada
            for (WhatsAppEntry entry : webhookPayload.getEntry()) {
                processEntry(entry);
            }

            return ResponseEntity.ok("OK");

        } catch (Exception e) {
            logger.error("Error procesando webhook: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }

    private void processEntry(WhatsAppEntry entry) {
        for (WhatsAppChange change : entry.getChanges()) {
            if ("messages".equals(change.getField())) {
                processMessages(change.getValue());
            }
        }
    }

    private void processMessages(WhatsAppValue value) {
        if (value.getMessages() != null) {
            for (WhatsAppMessage message : value.getMessages()) {
                processMessage(message, value);
            }
        }
    }

    private void processMessage(WhatsAppMessage message, WhatsAppValue value) {
        String messageId = message.getId();
        String from = message.getFrom();
        String timestamp = message.getTimestamp();
        String type = message.getType();

        // Obtener información del contacto
        String contactName = getContactName(value.getContacts(), message.getFrom());

        logger.info("Mensaje recibido - ID: {}, From: {}, Type: {}, Contact: {}",
                messageId, from, type, contactName);

        switch (type) {
            case "text":
                processTextMessage(message, from, contactName);
                break;
            case "interactive":
                processInteractiveMessage(message, from, contactName);
                break;
            case "image":
                processImageMessage(message, from, contactName);
                break;
            case "audio":
                processAudioMessage(message, from, contactName);
                break;
            case "document":
                processDocumentMessage(message, from, contactName);
                break;
            default:
                logger.info("Tipo de mensaje no manejado: {}", type);
                break;
        }
    }

    private void processTextMessage(WhatsAppMessage message, String from, String contactName) {
        String textContent = message.getText().getBody();

        logger.info("Mensaje de texto de {}: {}", contactName, textContent);

        // Procesar el mensaje de texto
        whatsAppService.handleTextMessage(from, contactName, textContent, message.getId());
    }

    private void processInteractiveMessage(WhatsAppMessage message, String from, String contactName) {
        WhatsAppInteractive interactive = message.getInteractive();
        String interactiveType = interactive.getType();

        logger.info("Mensaje interactivo de {} - Tipo: {}", contactName, interactiveType);

        switch (interactiveType) {
            case "list_reply":
                processListReply(interactive.getListReply(), from, contactName, message.getId());
                break;
            case "button_reply":
                processButtonReply(interactive.getButtonReply(), from, contactName, message.getId());
                break;
            default:
                logger.info("Tipo de mensaje interactivo no manejado: {}", interactiveType);
                break;
        }
    }

    private void processListReply(WhatsAppListReply listReply, String from, String contactName, String messageId) {
        String selectedId = listReply.getId();
        String selectedTitle = listReply.getTitle();
        String selectedDescription = listReply.getDescription();

        logger.info("Lista seleccionada por {} - ID: {}, Título: {}, Descripción: {}",
                contactName, selectedId, selectedTitle, selectedDescription);

        // Procesar la opción seleccionada de la lista
        whatsAppService.handleListSelection(from, contactName, selectedId, selectedTitle, selectedDescription, messageId);
    }

    private void processButtonReply(WhatsAppButtonReply buttonReply, String from, String contactName, String messageId) {
        String selectedId = buttonReply.getId();
        String selectedTitle = buttonReply.getTitle();

        logger.info("Botón seleccionado por {} - ID: {}, Título: {}",
                contactName, selectedId, selectedTitle);

        // Procesar el botón seleccionado
        whatsAppService.handleButtonSelection(from, contactName, selectedId, selectedTitle, messageId);
    }

    private void processImageMessage(WhatsAppMessage message, String from, String contactName) {
        WhatsAppMedia image = message.getImage();
        logger.info("Imagen recibida de {}: ID={}, MIME={}", contactName, image.getId(), image.getMimeType());

        whatsAppService.handleImageMessage(from, contactName, image, message.getId());
    }

    private void processAudioMessage(WhatsAppMessage message, String from, String contactName) {
        WhatsAppMedia audio = message.getAudio();
        logger.info("Audio recibido de {}: ID={}, MIME={}", contactName, audio.getId(), audio.getMimeType());

        whatsAppService.handleAudioMessage(from, contactName, audio, message.getId());
    }

    private void processDocumentMessage(WhatsAppMessage message, String from, String contactName) {
        WhatsAppMedia document = message.getDocument();
        logger.info("Documento recibido de {}: ID={}, Filename={}", contactName, document.getId(), document.getFilename());

        whatsAppService.handleDocumentMessage(from, contactName, document, message.getId());
    }

    private String getContactName(List<WhatsAppContact> contacts, String phoneNumber) {
        if (contacts != null) {
            for (WhatsAppContact contact : contacts) {
                if (phoneNumber.equals(contact.getWaId())) {
                    return contact.getProfile() != null ? contact.getProfile().getName() : phoneNumber;
                }
            }
        }
        return phoneNumber;
    }

    private boolean verifySignature(String payload, String signature) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(webhookSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKeySpec);

            byte[] hash = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            String expectedSignature = "sha256=" + hexString.toString();
            return expectedSignature.equals(signature);

        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            logger.error("Error verificando firma: ", e);
            return false;
        }
    }

//    @PostMapping
//    public ResponseEntity<String> receiveWebhook(@RequestBody WhatsAppMessageRequest payload) {
//
//        log.info("Received webhook payload");
//        log.info("Payload: {}", payload.toString());
//        CompletableFuture.runAsync(() -> {
//            try {
//                whatsAppService.handleIncomingMessage(payload);
//            } catch (Exception e) {
//                log.error("Error processing message asynchronously", e);
//            }
//        });
//        return ResponseEntity.ok("EVENT_RECEIVED");
//    }
//
//    @GetMapping
//    public ResponseEntity<String> verifyWebhook(
//            @RequestParam("hub.mode") String mode,
//            @RequestParam("hub.verify_token") String verifyToken,
//            @RequestParam("hub.challenge") String challenge) {
//
//        System.out.println("Entered verifyWebhook method");
//
//        if ("subscribe".equals(mode) && "tu_token_de_verificacion".equals(verifyToken)) {
//            return ResponseEntity.ok(challenge);
//        } else {
//            return ResponseEntity.status(403).body("Forbidden");
//        }
//    }



}
