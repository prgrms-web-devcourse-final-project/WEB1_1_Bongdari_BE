package com.somemore.user.usecase;

import com.somemore.user.dto.request.ImgUrlRequestDto;

import java.util.UUID;

public interface UpdateProfileImgUrlUseCase {
    String update(UUID userId, ImgUrlRequestDto dto);
}
