package com.somemore.volunteer.repository.record;

import java.util.UUID;

public record VolunteerNicknameAndId(
        UUID id,
        UUID userId,
        String nickname
) {
}
