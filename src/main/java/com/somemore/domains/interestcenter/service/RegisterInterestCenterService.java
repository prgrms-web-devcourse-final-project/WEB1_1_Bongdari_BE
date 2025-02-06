package com.somemore.domains.interestcenter.service;

import static com.somemore.global.exception.ExceptionMessage.DUPLICATE_INTEREST_CENTER;

import com.somemore.center.usecase.NEWCenterQueryUseCase;
import com.somemore.domains.interestcenter.domain.InterestCenter;
import com.somemore.domains.interestcenter.dto.request.RegisterInterestCenterRequestDto;
import com.somemore.domains.interestcenter.dto.response.RegisterInterestCenterResponseDto;
import com.somemore.domains.interestcenter.repository.InterestCenterRepository;
import com.somemore.domains.interestcenter.usecase.RegisterInterestCenterUseCase;
import com.somemore.global.exception.DuplicateException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class RegisterInterestCenterService implements RegisterInterestCenterUseCase {

    private final InterestCenterRepository interestCenterRepository;
    private final NEWCenterQueryUseCase centerQueryUseCase;

    @Override
    public RegisterInterestCenterResponseDto registerInterestCenter(
            UUID volunteerId,
            RegisterInterestCenterRequestDto requestDto
    ) {
        centerQueryUseCase.validateCenterExists(requestDto.centerId());
        boolean isDuplicate = interestCenterRepository.existsByVolunteerIdAndCenterId(volunteerId,
                requestDto.centerId());
        if (isDuplicate) {
            throw new DuplicateException(DUPLICATE_INTEREST_CENTER);
        }

        InterestCenter interestCenter = interestCenterRepository.save(
                requestDto.toEntity(volunteerId));

        return RegisterInterestCenterResponseDto.from(interestCenter);
    }

}
