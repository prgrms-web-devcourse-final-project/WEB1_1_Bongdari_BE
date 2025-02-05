package com.somemore.user.dto.request;

import java.util.UUID;

public record UpdateProfileImgUrlRequestDto(
        UUID userId,
        String profileImgUrl
) {
}
