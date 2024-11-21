package com.somemore.domains;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Community_board")
public class CommunityBoard {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "writer_id", nullable = false, length = 16)
    private String writerId;

    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "img_url")
    private String imgUrl;

}