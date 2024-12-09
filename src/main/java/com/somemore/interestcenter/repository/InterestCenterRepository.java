package com.somemore.interestcenter.repository;

import com.somemore.interestcenter.domain.InterestCenter;
import com.somemore.interestcenter.dto.response.RegisterInterestCenterResponseDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InterestCenterRepository {
    InterestCenter save(InterestCenter interestCenter);
    Optional<InterestCenter> findById(Long id);
    Optional<RegisterInterestCenterResponseDto> findInterestCenterResponseById(Long id);
    List<UUID> findInterestCenterIdsByVolunteerId(UUID volunteerId);
    boolean existsByVolunteerIdAndCenterId(UUID volunteerId, UUID centerId);
    Optional<InterestCenter> findByVolunteerIdAndCenterId(UUID volunteerId, UUID centerId);
}
