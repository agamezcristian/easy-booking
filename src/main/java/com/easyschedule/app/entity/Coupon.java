package com.easyschedule.app.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

@Entity
@Table(name = "cupones")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false, length = 50)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "tipo_descuento")
    private DiscountType discountType;

    @Column(name = "valor_descuento", nullable = false, precision = 10, scale = 2)
    private BigDecimal discountValue;

    @Column
    private Integer maxUses;

    @Column(name = "usos_maximos_por_usuario")
    private Integer maxUsesPerUser = 1;

    @Column(name = "usos_actuales")
    private Integer currentUses = 0;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate startDate;

    @Column(name = "fecha_expiracion")
    private LocalDate expirationDate;

    @Column(columnDefinition = "json")
    private String applicablePlans;

    @Column(columnDefinition = "json")
    private String applicableCycles;

    @Column(name = "solo_nuevos_usuarios")
    private Boolean onlyNewUsers = false;

    @Column(name = "es_acumulable")
    private Boolean stackable = false;

    @Column
    private Boolean active = true;

    @Column(name = "creado_por")
    private Integer createdBy;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    public enum DiscountType {
        porcentaje, monto_fijo
    }
}