package com.somemore.interestcenter.repository;

import com.somemore.interestcenter.domain.InterestCenter;
import com.somemore.interestcenter.dto.response.RegisterInterestCenterResponseDto;

import java.util.Optional;
import java.util.UUID;

public interface InterestCenterRepository {
    InterestCenter save(InterestCenter interestCenter);
    Optional<InterestCenter> findById(Long id);
    Optional<RegisterInterestCenterResponseDto> findInterestCenterResponseById(Long id);
    boolean existsByVolunteerIdAndCenterId(UUID volunteerId, UUID centerId);
}
