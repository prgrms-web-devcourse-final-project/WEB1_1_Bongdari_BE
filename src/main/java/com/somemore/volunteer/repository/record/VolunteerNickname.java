package com.somemore.volunteer.repository.record;

import java.util.UUID;

public record VolunteerNickname(
        UUID volunteerId,
        String nickname
) {
}
