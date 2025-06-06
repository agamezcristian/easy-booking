package com.easyschedule.app.dto.whatsapp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class WhatsAppContact {
    private WhatsAppProfile profile;
    @JsonProperty("wa_id")
    private String waId;
}
