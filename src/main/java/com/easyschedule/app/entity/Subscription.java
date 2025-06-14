package com.easyschedule.app.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

@Entity
@Table(name = "suscripciones")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "usuario_id", nullable = false)
    private Integer userId;

    @ManyToOne
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    @ManyToOne
    @JoinColumn(name = "plan_precio_id", nullable = false)
    private PlanPrice planPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.activa;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate startDate;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate endDate;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal originalPrice;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal finalPrice;

    @OneToOne
    @JoinColumn(name = "cupon_aplicado_id")
    private Coupon appliedCoupon;

    @Column(columnDefinition = "json")
    private String automaticDiscounts;

    @Column(name = "renovacion_automatica")
    private Boolean renewal = true;

    @Column(name = "gateway_suscripcion_id")
    private String gatewayId;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    public enum Status {
        activa, cancelada, suspendida, expirada
    }
}
