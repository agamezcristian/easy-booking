package com.easyschedule.app.entity;


import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = "whatsapp_configuration", indexes = {
        @Index(name = "idx_whatsapp_config_business_id", columnList = "business_id")
})
public class WhatsappConfiguration extends EasyBookingBaseEntity {

    @Column(name = "business_id", length = 32, nullable = false, unique = true)
    private String businessId;

    @Column(name = "phone_number", length = 20, nullable = false)
    private String phoneNumber;

    @Column(name = "phone_number_id", length = 50, nullable = false, unique = true)
    private String phoneNumberId;

    @Column(name = "whatsapp_business_account_id", length = 50, nullable = false)
    private String whatsappBusinessAccountId;

    @Lob
    @Column(name = "access_token", nullable = false, columnDefinition = "TEXT")
    private String accessToken;

    @Column(name = "webhook_verify_token", length = 255)
    private String webhookVerifyToken;

    @Column(name = "webhook_url", length = 500)
    private String webhookUrl;
}
