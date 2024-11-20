package com.somemore.community.usecase;

import com.somemore.community.dto.request.CommunityCreateRequestDto;

import java.util.UUID;

public interface CreateCommunityUseCase {
    Long createCommunityBoard(
        CommunityCreateRequestDto requestDto,
        UUID writerId,
        String imgUrl);
}
