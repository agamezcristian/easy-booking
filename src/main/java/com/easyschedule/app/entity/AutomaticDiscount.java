package com.easyschedule.app.entity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

@Entity
@Table(name = "descuentos_automaticos")
public class AutomaticDiscount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "condicion_tipo", nullable = false)
    private ConditionType conditionType;

    @Column(name = "condicion_valor", nullable = false, length = 100)
    private String conditionValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_descuento", nullable = false)
    private DiscountType discountType;

    @Column(name = "valor_descuento", nullable = false, precision = 10, scale = 2)
    private BigDecimal discountValue;

    @Column(columnDefinition = "json")
    private String applicablePlans;

    @Column(name = "es_acumulable")
    private Boolean stackable = false;

    @Column
    private Integer priority = 1;

    @Column
    private Boolean active = true;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    public enum ConditionType {
        ciclo_facturacion, cantidad_meses, plan_especifico, nuevo_usuario
    }
    public enum DiscountType {
        porcentaje, monto_fijo
    }
}
