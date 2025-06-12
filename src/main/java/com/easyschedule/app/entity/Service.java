package com.easyschedule.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = "service")
public class Service extends EasyBookingBaseEntity {

    @ManyToOne
    @JoinColumn(name = "business_id")
    private Business business;

    private String name;
    private String description;
    private Integer duration;
    private BigDecimal price;
    private String status;

}

