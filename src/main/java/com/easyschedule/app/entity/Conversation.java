package com.easyschedule.app.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@SuperBuilder
@Entity
@Table(name = "conversation", indexes = {
        @Index(name = "idx_conversation_customer_id", columnList = "customer_id"),
        @Index(name = "idx_conversation_business_id", columnList = "business_id"),
        @Index(name = "idx_conversation_channel_id", columnList = "channel_id"),
        @Index(name = "idx_conversation_status", columnList = "status")
})
public class Conversation extends EasyBookingBaseEntity {

    @Column(name = "business_id", length = 32, nullable = false)
    private String businessId;

    @Column(name = "customer_id", length = 32, nullable = false)
    private String customerId;

    @Column(name = "channel_id", length = 32, nullable = false)
    private String channelId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ConversationStatus status;

    @Column(name = "conversation_start_at", nullable = false)
    private LocalDateTime conversationStartAt;

    @Column(name = "conversation_end_at")
    private LocalDateTime conversationEndAt;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Message> messages;

    @OneToOne(mappedBy = "conversation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ConversationSession conversationSession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Channel channel;
}
