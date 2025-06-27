package com.easyschedule.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@SuperBuilder
@Entity
@Table(name = "appointment", indexes = {
        @Index(name = "idx_appointment_business_id", columnList = "business_id"),
        @Index(name = "idx_appointment_customer_id", columnList = "customer_id"),
        @Index(name = "idx_appointment_status", columnList = "status")
})
public class Appointment extends EasyBookingBaseEntity {

    @Column(name = "business_id", length = 32, nullable = false)
    private String businessId;

    @Column(name = "customer_id", length = 32, nullable = false)
    private String customerId;

    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;

    @Column(name = "appointment_time", nullable = false)
    private LocalTime appointmentTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AppointmentStatus status;

    @Column(name = "send_reminder")
    private Boolean sendReminder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Customer customer;


}



