package com.easyschedule.app.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

@Entity
@Table(name = "transacciones")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(length = 3)
    private String currency = "USD";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.pendiente;

    @Column(length = 50)
    private String gateway;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "fecha_procesamiento")
    @Temporal(TemporalType.TIMESTAMP)
    private Date processedAt;

    @Column(columnDefinition = "json")
    private String metadata;

    @ManyToOne
    @JoinColumn(name = "suscripcion_id", nullable = false)
    private Subscription subscription;

    public enum Type { pago, reembolso, ajuste }
    public enum Status { pendiente, completado, fallido, cancelado }
}

