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

            whatsAppService.processMessages(payload);

            return ResponseEntity.ok("OK");

        } catch (Exception e) {
            logger.error("Error procesando webhook: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
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
