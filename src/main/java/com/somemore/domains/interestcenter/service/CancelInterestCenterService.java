package com.somemore.domains.interestcenter.service;

import static com.somemore.global.exception.ExceptionMessage.CANNOT_CANCEL_DELETED_INTEREST_CENTER;

import com.somemore.center.usecase.NEWCenterQueryUseCase;
import com.somemore.domains.interestcenter.domain.InterestCenter;
import com.somemore.domains.interestcenter.repository.InterestCenterRepository;
import com.somemore.domains.interestcenter.usecase.CancelInterestCenterUseCase;
import com.somemore.global.exception.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class CancelInterestCenterService implements CancelInterestCenterUseCase {

    private final InterestCenterRepository interestCenterRepository;
    private final NEWCenterQueryUseCase centerQueryUseCase;

    @Override
    public void cancelInterestCenter(UUID volunteerId, UUID centerId) {
        centerQueryUseCase.validateCenterExists(centerId);

        InterestCenter interestCenter = interestCenterRepository.findByVolunteerIdAndCenterId(
                        volunteerId, centerId)
                .orElseThrow(
                        () -> new NoSuchElementException(CANNOT_CANCEL_DELETED_INTEREST_CENTER));

        interestCenter.markAsDeleted();

    }

}
