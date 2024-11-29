package com.somemore.interestcenter.service;

import com.somemore.global.exception.BadRequestException;
import com.somemore.interestcenter.domain.InterestCenter;
import com.somemore.interestcenter.repository.InterestCenterRepository;
import com.somemore.interestcenter.usecase.CancelInterestCenterUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.somemore.global.exception.ExceptionMessage.CANNOT_CANCEL_DELETED_INTEREST_CENTER;

@RequiredArgsConstructor
@Service
public class CancelInterestCenterService implements CancelInterestCenterUseCase {

    private final InterestCenterRepository interestCenterRepository;

    @Override
    public void cancelInterestCenter(Long interestCenterId) {
        InterestCenter interestCenter = interestCenterRepository.findById(interestCenterId)
                .orElseThrow(() -> new BadRequestException(CANNOT_CANCEL_DELETED_INTEREST_CENTER.getMessage()));

        interestCenter.markAsDeleted();

        interestCenterRepository.save(interestCenter);
    }

}
