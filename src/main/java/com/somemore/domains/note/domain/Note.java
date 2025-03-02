package com.somemore.domains.note.domain;

import com.somemore.global.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

import static jakarta.persistence.GenerationType.IDENTITY;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "note")
public class Note extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "sender_id", nullable = false, length = 16)
    private UUID senderId;

    @Column(name = "receiver_id", nullable = false, length = 16)
    private UUID receiverId;

    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Column(name = "content", length = 500, nullable = false)
    private String content;

    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;

    @Builder
    private Note(UUID senderId, UUID receiverId, String title, String content) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.title = title;
        this.content = content;
    }

    public static Note create(UUID senderId, UUID receiverId, String title, String content) {
        return Note.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .title(title)
                .content(content)
                .build();
    }

    public void markAsRead() {
        this.isRead = true;
    }

}
