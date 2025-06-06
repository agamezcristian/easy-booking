package com.easyschedule.app.service;


import com.easyschedule.app.dto.WhatsAppMessageRequest;
import com.easyschedule.app.dto.whatsapp.WhatsAppMedia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class WhatsAppService {

    private static final Logger logger = LoggerFactory.getLogger(WhatsAppService.class);

    @Value("${whatsapp.api.url}")
    private String whatsappApiUrl;

    @Value("${whatsapp.api.token}")
    private String whatsappApiToken;

    @Value("${whatsapp.api.numberid}")
    private String numberId;

    private final WebClient webClient = WebClient.builder().build();

    /**
     * Maneja mensajes de texto recibidos
     */
    public void handleTextMessage(String from, String contactName, String textContent, String messageId) {
        logger.info("Procesando mensaje de texto de {}: {}", contactName, textContent);

        // Ejemplo de lógica de negocio basada en el contenido del mensaje
        String response = processTextInput(textContent.toLowerCase().trim());

        // Aquí puedes guardar el mensaje en base de datos, procesarlo con IA, etc.
        // saveMessageToDatabase(from, contactName, textContent, messageId, "text");

        // Enviar respuesta si es necesario
        if (response != null) {
            // sendTextMessage(from, response);
            logger.info("Respuesta generada para {}: {}", contactName, response);
        }
    }

    /**
     * Maneja selecciones de lista interactiva
     */
    public void handleListSelection(String from, String contactName, String selectedId,
                                    String selectedTitle, String selectedDescription, String messageId) {

        logger.info("Procesando selección de lista de {}: ID={}, Title={}",
                contactName, selectedId, selectedTitle);

        // Procesar la opción seleccionada
        String response = processListSelection(selectedId, selectedTitle);

        // Guardar la selección en base de datos
        // saveInteractionToDatabase(from, contactName, selectedId, selectedTitle, messageId, "list_reply");

        // Enviar respuesta basada en la selección
        if (response != null) {
            // sendTextMessage(from, response);
            logger.info("Respuesta a selección de lista para {}: {}", contactName, response);
        }
    }

    /**
     * Maneja selecciones de botón interactivo
     */
    public void handleButtonSelection(String from, String contactName, String selectedId,
                                      String selectedTitle, String messageId) {

        logger.info("Procesando selección de botón de {}: ID={}, Title={}",
                contactName, selectedId, selectedTitle);

        // Procesar el botón seleccionado
        String response = processButtonSelection(selectedId, selectedTitle);

        // Guardar la selección en base de datos
        // saveInteractionToDatabase(from, contactName, selectedId, selectedTitle, messageId, "button_reply");

        // Enviar respuesta basada en la selección
        if (response != null) {
            // sendTextMessage(from, response);
            logger.info("Respuesta a selección de botón para {}: {}", contactName, response);
        }
    }

    /**
     * Maneja mensajes de imagen
     */
    public void handleImageMessage(String from, String contactName, WhatsAppMedia image, String messageId) {
        logger.info("Procesando imagen de {}: ID={}", contactName, image.getId());

        // Aquí puedes descargar la imagen usando la Graph API de Meta
        // String imageUrl = downloadMediaFile(image.getId());
        // Procesar la imagen (OCR, análisis, etc.)

        // saveMessageToDatabase(from, contactName, image.getId(), messageId, "image");
    }

    /**
     * Maneja mensajes de audio
     */
    public void handleAudioMessage(String from, String contactName, WhatsAppMedia audio, String messageId) {
        logger.info("Procesando audio de {}: ID={}", contactName, audio.getId());

        // Aquí puedes descargar el audio y convertirlo a texto
        // String audioUrl = downloadMediaFile(audio.getId());
        // String transcription = transcribeAudio(audioUrl);

        // saveMessageToDatabase(from, contactName, audio.getId(), messageId, "audio");
    }

    /**
     * Maneja mensajes de documento
     */
    public void handleDocumentMessage(String from, String contactName, WhatsAppMedia document, String messageId) {
        logger.info("Procesando documento de {}: ID={}, Filename={}",
                contactName, document.getId(), document.getFilename());

        // Aquí puedes descargar y procesar el documento
        // String documentUrl = downloadMediaFile(document.getId());
        // String content = extractDocumentContent(documentUrl, document.getMime_type());

        // saveMessageToDatabase(from, contactName, document.getId(), messageId, "document");
    }

    /**
     * Procesa entrada de texto y genera respuesta
     */
    private String processTextInput(String textContent) {
        // Ejemplo de lógica de chatbot simple
        if (!textContent.isEmpty()) {
            return "¡Hola! ¿En qué puedo ayudarte hoy?";
        } else if (textContent.contains("ayuda") || textContent.contains("help")) {
            return "Estoy aquí para ayudarte. Puedes preguntarme sobre nuestros servicios o productos.";
        } else if (textContent.contains("gracias")) {
            return "¡De nada! ¿Hay algo más en lo que pueda ayudarte?";
        } else if (textContent.contains("menu") || textContent.contains("opciones")) {
            // Aquí podrías enviar una lista interactiva
            return "Te muestro nuestras opciones principales...";
        }

        // Si no hay coincidencia, podrías usar IA para generar respuesta
        return generateAIResponse(textContent);
    }

    /**
     * Procesa selección de lista y genera respuesta
     */
    private String processListSelection(String selectedId, String selectedTitle) {
        switch (selectedId) {
            case "option_products":
                return "Has seleccionado ver productos. Aquí tienes nuestro catálogo...";
            case "option_services":
                return "Has seleccionado servicios. Te muestro lo que ofrecemos...";
            case "option_support":
                return "Has seleccionado soporte. Un agente te atenderá pronto.";
            case "option_info":
                return "Has seleccionado información. ¿Qué te gustaría saber?";
            default:
                return "Opción seleccionada: " + selectedTitle + ". ¿En qué más puedo ayudarte?";
        }
    }

    /**
     * Procesa selección de botón y genera respuesta
     */
    private String processButtonSelection(String selectedId, String selectedTitle) {
        switch (selectedId) {
            case "btn_yes":
                return "Perfecto, continuemos con el proceso...";
            case "btn_no":
                return "Entendido, ¿hay algo más que pueda hacer por ti?";
            case "btn_more_info":
                return "Te proporciono más información...";
            case "btn_contact":
                return "Te conecto con un agente humano...";
            default:
                return "Has seleccionado: " + selectedTitle;
        }
    }

    /**
     * Genera respuesta usando IA (placeholder)
     */
    private String generateAIResponse(String textContent) {
        // Aquí integrarías con un servicio de IA como OpenAI, Dialogflow, etc.
        // Por ahora, respuesta genérica
        return "Gracias por tu mensaje. Un agente revisará tu consulta y te responderá pronto.";
    }

    /**
     * Envía mensaje de texto (placeholder - necesitarías implementar la API de envío)
     */
    private void sendTextMessage(String to, String message) {
        // Implementar llamada a WhatsApp Business API para enviar mensaje
        logger.info("Enviando mensaje a {}: {}", to, message);
    }

    /**
     * Descarga archivo multimedia (placeholder)
     */
    private String downloadMediaFile(String mediaId) {
        // Implementar descarga usando Graph API de Meta
        logger.info("Descargando archivo multimedia: {}", mediaId);
        return "https://example.com/media/" + mediaId;
    }

    public void handleIncomingMessage(WhatsAppMessageRequest payload) {
        try {

            var entry = payload.entry[0];
            var change = entry.changes[0];
            var message = change.value.messages[0];
            var from = message.from;
            var text = message.text.body;

            sendWhatsAppMessage(from, "Hola! Recibimos tu mensaje: " + text);
        } catch (Exception e) {
            System.out.println("Error procesando el mensaje: " + e.getMessage());
        }
    }

    public void sendWhatsAppMessage(String to, String msg) {
        // Construye el cuerpo según la API de WhatsApp Business (ejemplo para Cloud API)
//        Map<String, Object> body1 = Map.of(
//                "messaging_product", "whatsapp",
//                "to", to,
//                "type", "text",
//                "text", Map.of("body", msg)
//        );

        Map<String, Object> bodyInteractiveList = Map.of(
                "messaging_product", "whatsapp",
                "to", to,
                "type", "interactive",
                "interactive", Map.of(
                        "type", "list",
                        "header", Map.of(
                                "type", "text",
                                "text", "Nuestros Servicios"
                        ),
                        "body", Map.of(
                                "text", "Selecciona el servicio que necesitas:"
                        ),
                        "footer", Map.of(
                                "text", "Estamos aquí para ayudarte"
                        ),
                        "action", Map.of(
                                "button", "Ver opciones",
                                "sections", List.of(
                                        Map.of(
                                                "title", "Servicios Principales",
                                                "rows", List.of(
                                                        Map.of(
                                                                "id", "service_consulting",
                                                                "title", "Consultoría",
                                                                "description", "Asesoría especializada en tu área"
                                                        ),
                                                        Map.of(
                                                                "id", "service_development",
                                                                "title", "Desarrollo",
                                                                "description", "Desarrollo de software a medida"
                                                        ),
                                                        Map.of(
                                                                "id", "service_support",
                                                                "title", "Soporte Técnico",
                                                                "description", "Asistencia técnica 24/7"
                                                        )
                                                )
                                        ),
                                        Map.of(
                                                "title", "Servicios Adicionales",
                                                "rows", List.of(
                                                        Map.of(
                                                                "id", "service_training",
                                                                "title", "Capacitación",
                                                                "description", "Cursos y talleres especializados"
                                                        ),
                                                        Map.of(
                                                                "id", "service_maintenance",
                                                                "title", "Mantenimiento",
                                                                "description", "Mantenimiento preventivo y correctivo"
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );

        String url = String.format(whatsappApiUrl, numberId);

        // Llama a la API de WhatsApp de forma imperativa usando
        webClient.post()
                .uri(url)
                .header("Authorization", "Bearer " + whatsappApiToken)
                .header("Content-Type", "application/json")
                .bodyValue(bodyInteractiveList)
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(
                        response -> logger.info("WhatsApp response: {}", response),
                        error -> logger.error("Error sending WhatsApp message", error)
                );

    }

}
