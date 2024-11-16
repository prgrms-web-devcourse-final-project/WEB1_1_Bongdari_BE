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
public class Notification {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "receiver_id", nullable = false, length = 16)
    private String receiverId;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

}