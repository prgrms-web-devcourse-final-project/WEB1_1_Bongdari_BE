package com.somemore.volunteer.usecase;

import com.somemore.volunteer.dto.response.VolunteerProfileResponseDto;

import java.util.UUID;

public interface VolunteerQueryUseCase {

    VolunteerProfileResponseDto getMyProfile(UUID volunteerId);

    VolunteerProfileResponseDto getVolunteerProfile(UUID volunteerId);

    VolunteerProfileResponseDto getVolunteerDetailedProfile(UUID volunteerId, UUID centerId);

    UUID getVolunteerIdByOAuthId(String oAuthId);

    String getNicknameById(UUID id);
}
