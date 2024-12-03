package com.somemore.notification;

import com.somemore.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "notification")
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "receiver_id", nullable = false, length = 16)
    private UUID receiverId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "related_id", nullable = false)
    private Long relatedId;

    @Column(name = "read", nullable = false)
    private boolean read;

    void markAsRead() {
        this.read = true;
        markAsDeleted();
    }
}