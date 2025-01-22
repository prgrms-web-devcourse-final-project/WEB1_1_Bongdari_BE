package com.somemore.domains.volunteer.usecase;

import com.somemore.domains.volunteer.domain.Volunteer;
import com.somemore.domains.volunteer.dto.response.VolunteerRankingResponseDto;
import com.somemore.domains.volunteer.repository.mapper.VolunteerSimpleInfo;

import java.util.List;
import java.util.UUID;

public interface VolunteerQueryUseCase {

    UUID getVolunteerIdByOAuthId(String oAuthId);

    String getNicknameById(UUID id);

    VolunteerRankingResponseDto getRankingByHours();

    List<Volunteer> getAllByIds(List<UUID> volunteerIds);

    List<VolunteerSimpleInfo> getVolunteerSimpleInfosByIds(List<UUID> ids);

    void validateVolunteerExists(UUID volunteerId);
}
