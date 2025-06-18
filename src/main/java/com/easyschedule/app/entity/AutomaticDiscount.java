package com.easyschedule.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = "automatic_discounts", indexes = {
        @Index(name = "idx_automatic_discounts_condition_type", columnList = "condition_type"),
        @Index(name = "idx_automatic_discounts_discount_type", columnList = "discount_type"),
        @Index(name = "idx_automatic_discounts_status", columnList = "status")
})
public class AutomaticDiscount extends EasyBookingBaseEntity {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "condition_type", nullable = false)
    private ConditionType conditionType;

    @Column(name = "condition_value", nullable = false, length = 100)
    private String conditionValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", nullable = false)
    private DiscountType discountType;

    @Column(name = "discount_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal discountValue;

    @Column(columnDefinition = "json")
    private String applicablePlans;

    @Column(name = "is_stackable")
    private Boolean stackable = false;

    @Column
    private Integer priority = 1;

    @Column
    private Boolean status = true;


    public enum ConditionType {
        billing_cycle, month_quantity, specific_plan, new_user
    }
    public enum DiscountType {
        percentage, fixed_amount
    }
}
