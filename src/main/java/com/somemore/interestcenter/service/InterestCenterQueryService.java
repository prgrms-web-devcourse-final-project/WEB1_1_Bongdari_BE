package com.somemore.interestcenter.service;

import com.somemore.center.repository.mapper.CenterOverviewInfo;
import com.somemore.center.usecase.query.CenterQueryUseCase;
import com.somemore.interestcenter.dto.response.InterestCentersResponseDto;
import com.somemore.interestcenter.repository.InterestCenterRepository;
import com.somemore.interestcenter.usecase.InterestCenterQueryUseCase;
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
}
