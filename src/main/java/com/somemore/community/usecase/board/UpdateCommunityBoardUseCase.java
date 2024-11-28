package com.somemore.community.usecase.board;

import com.somemore.community.dto.request.CommunityBoardUpdateRequestDto;

import java.util.UUID;

public interface UpdateCommunityBoardUseCase {
    void updateCommunityBoard(
            CommunityBoardUpdateRequestDto requestDto,
            Long communityBoardId,
            UUID writerId,
            String imgUrl);
}
