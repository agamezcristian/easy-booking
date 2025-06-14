package com.easyschedule.app.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

@Entity
@Table(name = "cupon_usos")
public class CouponUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "cupon_id", nullable = false)
    private Coupon coupon;

    @Column(name = "usuario_id", nullable = false)
    private Integer userId;

    @ManyToOne
    @JoinColumn(name = "suscripcion_id", nullable = false)
    private Subscription subscription;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal discount;

    @Column(name = "fecha_uso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date usedAt;
}
