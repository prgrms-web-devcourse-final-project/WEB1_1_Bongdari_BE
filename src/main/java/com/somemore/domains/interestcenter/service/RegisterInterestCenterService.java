package com.somemore.domains.interestcenter.service;

import com.somemore.domains.center.usecase.query.CenterQueryUseCase;
import com.somemore.domains.interestcenter.domain.InterestCenter;
import com.somemore.domains.interestcenter.dto.request.RegisterInterestCenterRequestDto;
import com.somemore.domains.interestcenter.dto.response.RegisterInterestCenterResponseDto;
import com.somemore.domains.interestcenter.repository.InterestCenterRepository;
import com.somemore.domains.interestcenter.usecase.RegisterInterestCenterUseCase;
import com.somemore.global.exception.DuplicateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.somemore.global.exception.ExceptionMessage.DUPLICATE_INTEREST_CENTER;

@RequiredArgsConstructor
@Service
public class RegisterInterestCenterService implements RegisterInterestCenterUseCase {

    private final InterestCenterRepository repository;
    private final CenterQueryUseCase centerQueryUseCase;

    @Override
    public RegisterInterestCenterResponseDto registerInterestCenter(UUID volunteerId, RegisterInterestCenterRequestDto requestDto) {

        centerQueryUseCase.validateCenterExists(requestDto.centerId());

        boolean isDuplicate = repository.existsByVolunteerIdAndCenterId(volunteerId, requestDto.centerId());
        if(isDuplicate){
            throw new DuplicateException(DUPLICATE_INTEREST_CENTER.getMessage());
        }

        InterestCenter interestCenter = repository.save(requestDto.toEntity(volunteerId));

        return RegisterInterestCenterResponseDto.from(interestCenter);
    }

}
