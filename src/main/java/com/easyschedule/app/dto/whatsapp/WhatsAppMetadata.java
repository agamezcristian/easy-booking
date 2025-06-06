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
public class WhatsAppMetadata {
    @JsonProperty("display_phone_number")
    private String displayPhoneNumber;
    @JsonProperty("phone_number_id")
    private String phoneNumberId;
}
