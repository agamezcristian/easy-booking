package com.easyschedule.app.service;


import com.easyschedule.app.dto.WhatsAppMessageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

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

    public void sendWhatsAppMessage(String to, String message) {
        // Construye el cuerpo según la API de WhatsApp Business (ejemplo para Cloud API)
        Map<String, Object> body = Map.of(
                "messaging_product", "whatsapp",
                "to", to,
                "type", "text",
                "text", Map.of("body", message)
        );

        String url = String.format(whatsappApiUrl, numberId);

        // Llama a la API de WhatsApp de forma imperativa usando
        webClient.post()
                .uri(url)
                .header("Authorization", "Bearer " + whatsappApiToken)
                .header("Content-Type", "application/json")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(
                        response -> logger.info("WhatsApp response: {}", response),
                        error -> logger.error("Error sending WhatsApp message", error)
                );

    }

}
