package com.somemore.domains.community.domain;

import com.somemore.domains.community.dto.request.CommunityCommentUpdateRequestDto;
import com.somemore.global.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
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

    @Column(name = "community_board_id", nullable = false)
    private Long communityBoardId;

    @Column(name = "writer_id", nullable = false, length = 16)
    private UUID writerId;

    @Lob
    @Column(name = "content", length = 200, nullable = false)
    private String content;

    @Column(name = "parent_comment_id")
    private Long parentCommentId;

    @Builder
    public CommunityComment(Long communityBoardId, UUID writerId, String content, Long parentCommentId) {
        this.communityBoardId = communityBoardId;
        this.writerId = writerId;
        this.content = content;
        this.parentCommentId = parentCommentId;
    }

    public boolean isWriter(UUID writerId) {
        return this.writerId.equals(writerId);
    }

    public void updateWith(CommunityCommentUpdateRequestDto dto) {
        this.content = dto.content();
    }

    @Override
    public void markAsDeleted() {
        super.markAsDeleted();
        this.content = "삭제된 댓글입니다";
    }

    public boolean isDeleted() {
        return this.getDeleted();
    }
}
