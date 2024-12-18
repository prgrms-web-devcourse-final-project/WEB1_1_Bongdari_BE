package com.somemore.domains.community.usecase.comment;

import com.somemore.domains.community.dto.request.CommunityCommentCreateRequestDto;

import java.util.UUID;

public interface CreateCommunityCommentUseCase {
    Long createCommunityComment(
            CommunityCommentCreateRequestDto requestDto,
            UUID writerId,
            Long communityBoardId);
}
