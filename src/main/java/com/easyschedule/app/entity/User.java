package com.easyschedule.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "user")
public class User extends EasyBookingBaseEntity {

    private String email;
    private String name;
    private String username;
    private String password;
    private Boolean status;

    @Column(name = "last_login_at")
    private LocalDateTime lastLogin;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;
}
