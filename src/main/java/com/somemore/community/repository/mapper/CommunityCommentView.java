package com.somemore.community.repository.mapper;

import com.somemore.community.domain.CommunityComment;
import lombok.Builder;

public record CommunityCommentView(
        CommunityComment communityComment,
        String writerNickname
) {
    @Builder
    public CommunityCommentView(CommunityComment communityComment, String writerNickname) {
        this.communityComment = communityComment;
        this.writerNickname = writerNickname;
    }

    public CommunityCommentView replaceWriterNickname(CommunityCommentView communityCommentView) {
        return CommunityCommentView.builder()
                .communityComment(communityCommentView.communityComment)
                .writerNickname("").build();
    }
}
