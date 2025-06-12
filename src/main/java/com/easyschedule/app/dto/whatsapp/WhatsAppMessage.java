package com.easyschedule.app.dto.whatsapp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class WhatsAppMessage {
    private String from;
    private String id;
    private String timestamp;
    private String type;
    private String to;
    private WhatsAppText text;
    private WhatsAppInteractive interactive;
    private WhatsAppMedia image;
    private WhatsAppMedia audio;
    private WhatsAppMedia video;
    private WhatsAppMedia document;
    private WhatsAppLocation location;
}
