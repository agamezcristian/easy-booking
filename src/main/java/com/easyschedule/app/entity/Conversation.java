package com.easyschedule.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = "conversation", indexes = {
        @Index(name = "idx_customer_id", columnList = "customer_id"),
        @Index(name = "idx_business_id", columnList = "business_id"),
        @Index(name = "idx_status", columnList = "status")
})
public class Conversation extends EasyBookingBaseEntity {


    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "business_id")
    private Business business;

    @Enumerated(EnumType.STRING)
    private ConversationStatus status;

    @Column(name = "conversation_start_at")
    private LocalDateTime conversationStartAt;

    @Column(name = "conversation_end_at")
    private LocalDateTime conversationEndAt;

    // Relaciones
    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Message> messages;

    @OneToOne(mappedBy = "conversation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ConversationSession conversationSession;
}
