package com.somemore.community.usecase;

import com.somemore.community.dto.request.CommunityBoardCreateRequestDto;

import java.util.UUID;

public interface CreateCommunityBoardUseCase {
    Long createCommunityBoard(
        CommunityBoardCreateRequestDto requestDto,
        UUID writerId,
        String imgUrl);
}
