package com.easyschedule.app.entity;

import jakarta.persistence.*;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = "role")
public class Role extends EasyBookingBaseEntity {

    private String name;
    private Boolean status;

}
