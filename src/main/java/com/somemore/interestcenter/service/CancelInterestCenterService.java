package com.somemore.interestcenter.service;

import com.somemore.center.usecase.query.CenterQueryUseCase;
import com.somemore.global.exception.BadRequestException;
import com.somemore.interestcenter.domain.InterestCenter;
import com.somemore.interestcenter.repository.InterestCenterRepository;
import com.somemore.interestcenter.usecase.CancelInterestCenterUseCase;
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
