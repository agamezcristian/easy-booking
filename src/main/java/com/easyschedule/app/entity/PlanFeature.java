package com.easyschedule.app.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

@Entity
@Table(name = "plan_caracteristicas",
        uniqueConstraints = @UniqueConstraint(columnNames = {"plan_id", "caracteristica"}))
public class PlanFeature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    @Column(nullable = false, length = 100)
    private String feature;

    @Column
    private String value;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type = Type.texto;

    public enum Type { numero, texto, booleano }
}

