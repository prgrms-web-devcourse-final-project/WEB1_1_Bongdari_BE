package com.somemore.center.repository.record;

import java.util.UUID;

public record CenterProfileDto(
        UUID id,
        UUID userId,
        String homepageUrl
) {
}
