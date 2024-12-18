package com.somemore.domains.volunteer.usecase;

import com.somemore.domains.volunteer.dto.request.VolunteerProfileUpdateRequestDto;

import java.util.UUID;

public interface UpdateVolunteerProfileUseCase {

    void update(UUID volunteerId, VolunteerProfileUpdateRequestDto requestDto, String imgUrl);
}
