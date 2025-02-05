package com.somemore.domains.interestcenter.service;

import com.somemore.center.repository.record.CenterOverviewInfo;
import com.somemore.center.usecase.NEWCenterQueryUseCase;
import com.somemore.domains.interestcenter.dto.response.InterestCentersResponseDto;
import com.somemore.domains.interestcenter.repository.InterestCenterRepository;
import com.somemore.domains.interestcenter.usecase.InterestCenterQueryUseCase;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class InterestCenterQueryService implements InterestCenterQueryUseCase {

    private final NEWCenterQueryUseCase centerQueryUseCase;
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
