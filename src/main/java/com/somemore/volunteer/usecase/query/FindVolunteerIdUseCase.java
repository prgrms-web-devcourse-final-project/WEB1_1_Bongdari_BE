package com.somemore.volunteer.usecase.query;

import com.somemore.volunteer.dto.response.VolunteerForCommunityResponseDto;

import java.util.UUID;

public interface FindVolunteerIdUseCase {
    UUID findVolunteerIdByOAuthId(String oAuthId);
    String getNicknameById(UUID id);
    VolunteerForCommunityResponseDto getVolunteerDetailForCommunity(UUID id);
}
