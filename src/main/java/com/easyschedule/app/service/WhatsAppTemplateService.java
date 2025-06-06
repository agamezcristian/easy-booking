package com.easyschedule.app.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class WhatsAppTemplateService {

    private static final Logger logger = LoggerFactory.getLogger(WhatsAppTemplateService.class);
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{\\{(\\w+)\\}\\}");

    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;

    private Map<String, JsonNode> templates = new HashMap<>();

    @PostConstruct
    public void loadTemplates() {
        try {
            loadTemplate("welcome-message", "classpath:templates/whatsapp/welcome-message.json");
            loadTemplate("authorization-denied", "classpath:templates/whatsapp/authorization-denied.json");
            loadTemplate("request-name", "classpath:templates/whatsapp/request-name.json");
            loadTemplate("main-menu", "classpath:templates/whatsapp/main-menu.json");
            loadTemplate("schedule-request-datetime", "classpath:templates/whatsapp/schedule-request-datetime.json");
            loadTemplate("schedule-confirmation", "classpath:templates/whatsapp/schedule-confirmation.json");
            loadTemplate("schedule-alternatives", "classpath:templates/whatsapp/schedule-alternatives.json");
            loadTemplate("schedule-success", "classpath:templates/whatsapp/schedule-success.json");
            loadTemplate("reschedule-list-appointments", "classpath:templates/whatsapp/reschedule-list-appointments.json");
            loadTemplate("reschedule-request-new-datetime", "classpath:templates/whatsapp/reschedule-request-new-datetime.json");
            loadTemplate("reschedule-success", "classpath:templates/whatsapp/reschedule-success.json");
            loadTemplate("cancel-list-appointments", "classpath:templates/whatsapp/cancel-list-appointments.json");
            loadTemplate("cancel-confirmation", "classpath:templates/whatsapp/cancel-confirmation.json");
            loadTemplate("cancel-success", "classpath:templates/whatsapp/cancel-success.json");
            loadTemplate("consult-appointments", "classpath:templates/whatsapp/consult-appointments.json");
            loadTemplate("no-appointments", "classpath:templates/whatsapp/no-appointments.json");
            loadTemplate("error-invalid-datetime", "classpath:templates/whatsapp/error-invalid-datetime.json");
            loadTemplate("error-past-datetime", "classpath:templates/whatsapp/error-past-datetime.json");
            loadTemplate("error-general", "classpath:templates/whatsapp/error-general.json");

            logger.info("Cargadas {} plantillas de WhatsApp", templates.size());
        } catch (Exception e) {
            logger.error("Error cargando plantillas de WhatsApp", e);
        }
    }

    private void loadTemplate(String templateName, String resourcePath) {
        try {
            Resource resource = resourceLoader.getResource(resourcePath);
            if (resource.exists()) {
                try (InputStream inputStream = resource.getInputStream()) {
                    JsonNode template = objectMapper.readTree(inputStream);
                    templates.put(templateName, template);
                    logger.debug("Plantilla '{}' cargada exitosamente", templateName);
                }
            } else {
                logger.warn("Plantilla no encontrada: {}", resourcePath);
            }
        } catch (IOException e) {
            logger.error("Error cargando plantilla '{}': {}", templateName, e.getMessage());
        }
    }

    /**
     * Construye un mensaje usando una plantilla y parámetros
     */
    public Map<String, Object> buildMessage(String templateName, String to, Map<String, String> params) {
        JsonNode template = templates.get(templateName);
        if (template == null) {
            throw new IllegalArgumentException("Plantilla no encontrada: " + templateName);
        }

        try {
            // Convertir template a Map para manipulación
            Map<String, Object> message = objectMapper
                    .convertValue(template, new TypeReference<Map<String, Object>>() {});

            // Agregar destinatario
            message.put("to", to);

            // Reemplazar placeholders si se proporcionan parámetros
            if (params != null && !params.isEmpty()) {
                message = replacePlaceholders(message, params);
            }

            return message;

        } catch (Exception e) {
            logger.error("Error construyendo mensaje con plantilla '{}': {}", templateName, e.getMessage());
            throw new RuntimeException("Error construyendo mensaje", e);
        }
    }

    /**
     * Construye un mensaje simple sin parámetros
     */
    public Map<String, Object> buildMessage(String templateName, String to) {
        return buildMessage(templateName, to, null);
    }

    /**
     * Reemplaza placeholders en el mensaje
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> replacePlaceholders(Map<String, Object> message, Map<String, String> params) {
        Map<String, Object> result = new HashMap<>();

        for (Map.Entry<String, Object> entry : message.entrySet()) {
            Object value = entry.getValue();

            if (value instanceof String) {
                // Reemplazar placeholders en strings
                result.put(entry.getKey(), replacePlaceholdersInString((String) value, params));
            } else if (value instanceof Map) {
                // Recursivo para mapas anidados
                result.put(entry.getKey(), replacePlaceholders((Map<String, Object>) value, params));
            } else {
                // Mantener otros tipos sin cambios
                result.put(entry.getKey(), value);
            }
        }

        return result;
    }

    /**
     * Reemplaza placeholders en un string individual
     */
    private String replacePlaceholdersInString(String text, Map<String, String> params) {
        if (!StringUtils.hasText(text)) {
            return text;
        }

        Matcher matcher = PLACEHOLDER_PATTERN.matcher(text);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String placeholder = matcher.group(1);
            String replacement = params.getOrDefault(placeholder, matcher.group(0));
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(result);

        return result.toString();
    }

    /**
     * Obtiene una plantilla específica (para inspección)
     */
    public JsonNode getTemplate(String templateName) {
        return templates.get(templateName);
    }

    /**
     * Lista todas las plantillas disponibles
     */
    public Map<String, JsonNode> getAllTemplates() {
        return new HashMap<>(templates);
    }

    /**
     * Recarga todas las plantillas
     */
    public void reloadTemplates() {
        templates.clear();
        loadTemplates();
    }

}
