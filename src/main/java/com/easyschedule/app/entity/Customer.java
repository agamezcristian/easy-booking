package com.easyschedule.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
@Table(name = "customer")
public class Customer extends EasyBookingBaseEntity {

    private String name;
    private String phone;
    private String email;

    @Column(name = "last_interaction")
    private LocalDateTime lastInteraction;

}

