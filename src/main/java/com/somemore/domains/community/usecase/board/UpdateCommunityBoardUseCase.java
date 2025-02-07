package com.somemore.domains.community.usecase.board;

import com.somemore.domains.community.dto.request.CommunityBoardUpdateRequestDto;

import java.util.UUID;

public interface UpdateCommunityBoardUseCase {
    void updateCommunityBoard(
            CommunityBoardUpdateRequestDto requestDto,
            Long communityBoardId,
            UUID writerId);
}
