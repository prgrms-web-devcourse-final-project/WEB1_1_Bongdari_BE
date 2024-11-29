package com.somemore.community.domain;

import com.somemore.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

import static lombok.AccessLevel.PROTECTED;


@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
@Table(name = "community_comment")
public class CommunityComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "writer_id", nullable = false, length = 16)
    private UUID writerId;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "parent_comment_id")
    private Long parentCommentId;

    @Builder
    public CommunityComment(UUID writerId, String content, Long parentCommentId) {
        this.writerId = writerId;
        this.content = content;
        this.parentCommentId = parentCommentId;
    }

    public boolean isWriter(UUID writerId) {
        return this.writerId.equals(writerId);
    }
}