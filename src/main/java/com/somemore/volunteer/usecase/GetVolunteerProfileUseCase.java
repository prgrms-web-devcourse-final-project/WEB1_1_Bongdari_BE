package com.somemore.volunteer.usecase;

import com.somemore.volunteer.dto.VolunteerProfileResponseDto;

import java.util.UUID;

public interface GetVolunteerProfileUseCase {

    VolunteerProfileResponseDto getProfileByUserId(UUID userId);

    VolunteerProfileResponseDto getProfileByVolunteerId(UUID volunteerId);
}
