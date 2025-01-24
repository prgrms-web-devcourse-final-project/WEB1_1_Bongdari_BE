package com.somemore.user.repository.usercommonattribute.record;

import java.util.UUID;

public record UserProfileDto(
        Long id,
        UUID userId,
        String name,
        String contactNumber,
        String imgUrl,
        String introduce
) {
}
