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
@Table(name = "conversation_sessions")
public class ConversationSession extends EasyBookingBaseEntity {

    @Column(name = "current_step", length = 100)
    private String currentStep;

    @Column(name = "last_activity")
    private LocalDateTime lastActivity;

    // Relaciones
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversacion_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Conversation conversation;
}
