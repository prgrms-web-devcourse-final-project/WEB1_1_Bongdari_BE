package com.somemore.community.repository.mapper;

import com.somemore.community.domain.CommunityComment;
import lombok.Builder;

@Builder
public record CommunityCommentView(
        CommunityComment communityComment,
        String writerNickname
) {
    public CommunityCommentView replaceWriterNickname(CommunityCommentView communityCommentView) {
        return CommunityCommentView.builder()
                .communityComment(communityCommentView.communityComment)
                .writerNickname("").build();
    }
}
