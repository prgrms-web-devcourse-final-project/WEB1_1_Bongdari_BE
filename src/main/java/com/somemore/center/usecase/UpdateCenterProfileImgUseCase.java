package com.somemore.center.usecase;

import com.somemore.center.dto.request.CenterProfileImgUpdateRequestDto;

import java.util.UUID;

public interface UpdateCenterProfileImgUseCase {
    String updateCenterProfileImg(UUID centerId, CenterProfileImgUpdateRequestDto requestDto);
}
