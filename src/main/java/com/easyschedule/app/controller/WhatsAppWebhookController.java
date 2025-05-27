package com.easyschedule.app.controller;

import com.easyschedule.app.dto.WhatsAppMessageRequest;
import com.easyschedule.app.service.WhatsAppService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.CompletableFuture;


@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/webhook")
public class WhatsAppWebhookController {


    private final WhatsAppService whatsAppService;

    @PostMapping
    public ResponseEntity<String> receiveWebhook(@RequestBody WhatsAppMessageRequest payload) {

        log.info("Received webhook payload");
        CompletableFuture.runAsync(() -> {
            try {
                whatsAppService.handleIncomingMessage(payload);
            } catch (Exception e) {
                log.error("Error processing message asynchronously", e);
            }
        });
        return ResponseEntity.ok("EVENT_RECEIVED");
    }

    @GetMapping
    public ResponseEntity<String> verifyWebhook(
            @RequestParam("hub.mode") String mode,
            @RequestParam("hub.verify_token") String verifyToken,
            @RequestParam("hub.challenge") String challenge) {

        if ("subscribe".equals(mode) && "tu_token_de_verificacion".equals(verifyToken)) {
            return ResponseEntity.ok(challenge);
        } else {
            return ResponseEntity.status(403).body("Forbidden");
        }
    }



}
