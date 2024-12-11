package com.somemore.interestcenter.usecase;

import com.somemore.interestcenter.dto.response.InterestCentersResponseDto;

import java.util.List;
import java.util.UUID;

public interface InterestCenterQueryUseCase {
    List<InterestCentersResponseDto> getInterestCenters(UUID volunteerId);
    List<UUID> getVolunteerIdsByCenterId(UUID centerId);
}
