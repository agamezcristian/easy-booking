package com.easyschedule.app.dto.whatsapp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class WhatsAppValue {
    @JsonProperty("messaging_product")
    private String messagingProduct;
    private WhatsAppMetadata metadata;
    private List<WhatsAppContact> contacts;
    private List<WhatsAppMessage> messages;
}
