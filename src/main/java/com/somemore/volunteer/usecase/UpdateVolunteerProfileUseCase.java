package com.somemore.volunteer.usecase;

import com.somemore.volunteer.dto.request.VolunteerProfileUpdateRequestDto;

import java.util.UUID;

public interface UpdateVolunteerProfileUseCase {

    void update(UUID volunteerId, VolunteerProfileUpdateRequestDto requestDto, String imgUrl);
}
