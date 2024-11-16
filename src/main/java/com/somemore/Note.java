package com.somemore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Note {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "sender_id", nullable = false, length = 16)
    private String senderId;

    @Column(name = "receiver_id", nullable = false, length = 16)
    private String receiverId;

    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "`read`", nullable = false)
    private Boolean read = false;

}