package com.somemore.volunteer.usecase;

import com.somemore.volunteer.dto.response.VolunteerResponseDto;

import java.util.UUID;

public interface VolunteerQueryUseCase {

    VolunteerResponseDto getMyProfile(UUID volunteerId);

    VolunteerResponseDto getVolunteerProfile(UUID volunteerId);

    VolunteerResponseDto getVolunteerDetailedProfile(UUID volunteerId, UUID centerId);

    UUID getVolunteerIdByOAuthId(String oAuthId);

    String getNicknameById(UUID id);
}
