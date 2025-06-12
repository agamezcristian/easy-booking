package com.easyschedule.app.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class EasyBookingBaseEntity {
    @Id
    @Column(length = 32, unique = true, nullable = false)
    protected String id;

    @Column(name = "CREATED_BY", nullable = false, length = 50, updatable = false)
    private String createdBy;

    @CreationTimestamp
    @Column(updatable = false, name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "LAST_MODIFIED_BY", length = 50)
    private String lastModifiedBy;

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = java.util.UUID.randomUUID().toString();
        }
        createdBy = "system";
    }

    @PreUpdate
    public void preUpdate() {
        lastModifiedBy = "system";
    }
}
