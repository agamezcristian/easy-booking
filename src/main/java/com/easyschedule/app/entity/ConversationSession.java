package com.easyschedule.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
@Table(name = "conversation_sessions", indexes = {
        @Index(name = "idx_conversation_sessions_conversacion_id", columnList = "conversacion_id"),
        @Index(name = "idx_conversation_sessions_current_step", columnList = "current_step")
})
public class ConversationSession extends EasyBookingBaseEntity {

    @Column(name = "conversation_id", length = 32, nullable = false)
    private String conversationId;

    @Column(name = "current_step", length = 100, nullable = false)
    private String currentStep;

    @Column(name = "last_activity", nullable = false)
    private LocalDateTime lastActivity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversacion_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Conversation conversation;
}
