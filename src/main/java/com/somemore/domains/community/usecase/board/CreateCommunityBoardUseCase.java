package com.somemore.domains.community.usecase.board;

import com.somemore.domains.community.dto.request.CommunityBoardCreateRequestDto;

import java.util.UUID;

public interface CreateCommunityBoardUseCase {
    Long createCommunityBoard(
        CommunityBoardCreateRequestDto requestDto,
        UUID writerId,
        String imgUrl);
}
