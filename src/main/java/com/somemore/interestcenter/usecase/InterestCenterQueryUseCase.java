package com.somemore.interestcenter.usecase;

import com.somemore.interestcenter.dto.response.GetInterestCentersResponseDto;

import java.util.List;
import java.util.UUID;

public interface InterestCenterQueryUseCase {
    List<GetInterestCentersResponseDto> getInterestCenters(UUID volunteerId);
}
