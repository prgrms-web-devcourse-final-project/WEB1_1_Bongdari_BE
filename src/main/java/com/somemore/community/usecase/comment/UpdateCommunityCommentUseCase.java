package com.somemore.community.usecase.comment;

import com.somemore.community.dto.request.CommunityCommentUpdateRequestDto;

import java.util.UUID;

public interface UpdateCommunityCommentUseCase {
    void updateCommunityComment(
            CommunityCommentUpdateRequestDto requestDto,
            Long communityCommentId,
            UUID writerId,
            Long communityBoardId);
}
