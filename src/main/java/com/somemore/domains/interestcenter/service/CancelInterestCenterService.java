package com.somemore.domains.interestcenter.service;

import com.somemore.domains.center.usecase.query.CenterQueryUseCase;
import com.somemore.domains.interestcenter.usecase.CancelInterestCenterUseCase;
import com.somemore.global.exception.BadRequestException;
import com.somemore.domains.interestcenter.domain.InterestCenter;
import com.somemore.domains.interestcenter.repository.InterestCenterRepository;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.somemore.global.exception.ExceptionMessage.CANNOT_CANCEL_DELETED_INTEREST_CENTER;

@RequiredArgsConstructor
@Service
public class CancelInterestCenterService implements CancelInterestCenterUseCase {

    private final InterestCenterRepository interestCenterRepository;
    private final CenterQueryUseCase centerQueryUseCase;

    @Override
    public void cancelInterestCenter(UUID volunteerId, UUID centerId) {
        centerQueryUseCase.validateCenterExists(centerId);

        InterestCenter interestCenter = interestCenterRepository.findByVolunteerIdAndCenterId(volunteerId, centerId)
                .orElseThrow(() -> new BadRequestException(CANNOT_CANCEL_DELETED_INTEREST_CENTER.getMessage()));

        interestCenter.markAsDeleted();

        interestCenterRepository.save(interestCenter);
    }

}
