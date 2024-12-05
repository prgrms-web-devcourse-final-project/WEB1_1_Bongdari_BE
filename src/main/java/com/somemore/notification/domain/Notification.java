package com.somemore.notification.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "receiver_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID receiverId;

    @Column(name = "title", nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private NotificationType type;

    @Column(name = "related_id", nullable = false)
    private Long relatedId;

    @Column(name = "is_read", nullable = false)
    private boolean read;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    void markAsRead() {
        this.read = true;
    }

    @Builder
    public Notification(
            UUID receiverId,
            String title,
            NotificationType type,
            Long relatedId
    ) {
        this.receiverId = receiverId;
        this.title = title;
        this.type = type;
        this.relatedId = relatedId;
        this.read = false;
    }
}