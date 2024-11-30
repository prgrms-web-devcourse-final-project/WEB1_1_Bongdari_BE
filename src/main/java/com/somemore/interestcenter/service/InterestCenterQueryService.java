package com.somemore.interestcenter.service;

import com.somemore.center.dto.response.CenterOverviewInfoResponseDto;
import com.somemore.center.usecase.query.CenterQueryUseCase;
import com.somemore.interestcenter.dto.response.GetInterestCentersResponseDto;
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
    public List<GetInterestCentersResponseDto> getInterestCenters(UUID volunteerId) {

        List<UUID> interestCenterIds = interestCenterRepository.findInterestCenterIdsByVolunteerId(volunteerId);
        if (interestCenterIds.isEmpty()) {
            return List.of();
        }

        List<CenterOverviewInfoResponseDto> centerOverviews = centerQueryUseCase.getCenterOverviewsByIds(interestCenterIds);
        return centerOverviews.stream()
                .map(GetInterestCentersResponseDto::of)
                .toList();
    }
}
