package com.somemore;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Community_comment")
public class CommunityComment {
    @EmbeddedId
    private CommunityCommentId id;

    @Column(name = "writer_id", nullable = false, length = 16)
    private String writerId;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "parent_comment_id")
    private Long parentCommentId;

}