package com.somemore.center.usecase.command;

import com.somemore.center.dto.request.CenterProfileUpdateRequestDto;

import java.util.UUID;

public interface UpdateCenterProfileUseCase {
    void updateCenterProfile(UUID centerId, CenterProfileUpdateRequestDto requestDto, String imgUrl);
}
