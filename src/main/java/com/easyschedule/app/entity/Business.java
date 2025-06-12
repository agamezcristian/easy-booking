package com.easyschedule.app.entity;

import jakarta.persistence.*;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "business")
public class Business extends EasyBookingBaseEntity {

    private String name;
    private String description;
    private String phone;
    private String email;
    private String status;
    private String country;
    private String city;
    private String state;

}

