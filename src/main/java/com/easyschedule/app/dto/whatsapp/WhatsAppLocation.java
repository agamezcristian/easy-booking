package com.easyschedule.app.dto.whatsapp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class WhatsAppLocation {
    private double latitude;
    private double longitude;
    private String name;
    private String address;
}
