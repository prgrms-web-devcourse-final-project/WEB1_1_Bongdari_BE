package com.somemore.support.fixture;

import com.somemore.domains.community.domain.CommunityComment;

import java.util.UUID;

public class CommunityCommentFixture {

    public static final String CONTENT = "커뮤니티 댓글 테스트 내용";

    private CommunityCommentFixture() {

    }

    public static CommunityComment createCommunityComment(Long communityBoardId, UUID writerId) {
        return CommunityComment.builder()
                .communityBoardId(communityBoardId)
                .content(CONTENT)
                .writerId(writerId)
                .parentCommentId(null)
                .build();
    }

    public static CommunityComment createCommunityComment(Long communityBoardId, UUID wrtierId, Long parentCommentId) {
        return CommunityComment.builder()
                .communityBoardId(communityBoardId)
                .content(CONTENT)
                .writerId(wrtierId)
                .parentCommentId(parentCommentId)
                .build();
    }

    public static CommunityComment createCommunityComment(String content, Long communityBoardId, UUID writerId) {
        return CommunityComment.builder()
                .communityBoardId(communityBoardId)
                .content(content)
                .writerId(writerId)
                .parentCommentId(null)
                .build();
    }
}
