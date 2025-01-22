package com.somemore.volunteer.usecase;

import com.somemore.volunteer.dto.VolunteerProfileResponseDto;

import java.util.UUID;

public interface GetVolunteerProfileUseCase {

    VolunteerProfileResponseDto getProfile(UUID userId);
}
