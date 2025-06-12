package com.easyschedule.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "customer")
public class Customer extends EasyBookingBaseEntity {

    private String name;
    private String phone;
    private String email;

    @Column(name = "last_interaction")
    private LocalDateTime lastInteraction;

}

