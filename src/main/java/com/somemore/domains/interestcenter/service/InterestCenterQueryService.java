package com.somemore.domains.interestcenter.service;

import com.somemore.domains.center.repository.mapper.CenterOverviewInfo;
import com.somemore.domains.center.usecase.query.CenterQueryUseCase;
import com.somemore.domains.interestcenter.usecase.InterestCenterQueryUseCase;
import com.somemore.domains.interestcenter.dto.response.InterestCentersResponseDto;
import com.somemore.domains.interestcenter.repository.InterestCenterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class InterestCenterQueryService implements InterestCenterQueryUseCase {

    private final CenterQueryUseCase centerQueryUseCase;
    private final InterestCenterRepository interestCenterRepository;

    @Override
    public List<InterestCentersResponseDto> getInterestCenters(UUID volunteerId) {

        List<UUID> interestCenterIds = interestCenterRepository.findInterestCenterIdsByVolunteerId(volunteerId);
        if (interestCenterIds.isEmpty()) {
            return List.of();
        }

        List<CenterOverviewInfo> centerOverviews = centerQueryUseCase.getCenterOverviewsByIds(interestCenterIds);
        return centerOverviews.stream()
                .map(InterestCentersResponseDto::of)
                .toList();
    }

    @Override
    public List<UUID> getVolunteerIdsByCenterId(UUID centerId) {

        return interestCenterRepository.findVolunteerIdsByCenterId(centerId);
    }
}
