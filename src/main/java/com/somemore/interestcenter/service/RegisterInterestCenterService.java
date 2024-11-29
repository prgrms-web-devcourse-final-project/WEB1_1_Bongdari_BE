package com.somemore.interestcenter.service;

import com.somemore.center.usecase.query.CenterQueryUseCase;
import com.somemore.global.exception.DuplicateException;
import com.somemore.interestcenter.domain.InterestCenter;
import com.somemore.interestcenter.dto.request.RegisterInterestCenterRequestDto;
import com.somemore.interestcenter.dto.response.RegisterInterestCenterResponseDto;
import com.somemore.interestcenter.repository.InterestCenterRepository;
import com.somemore.interestcenter.usecase.RegisterInterestCenterUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.somemore.global.exception.ExceptionMessage.DUPLICATE_INTEREST_CENTER;

@RequiredArgsConstructor
@Service
public class RegisterInterestCenterService implements RegisterInterestCenterUseCase {

    private final InterestCenterRepository repository;
    private final CenterQueryUseCase centerQueryUseCase;

    @Override
    public RegisterInterestCenterResponseDto registerInterestCenter(RegisterInterestCenterRequestDto requestDto) {

        centerQueryUseCase.validateCenterExists(requestDto.centerId());

        boolean isDuplicate = repository.existsByVolunteerIdAndCenterId(requestDto.volunteerId(), requestDto.centerId());
        if(isDuplicate){
            throw new DuplicateException(DUPLICATE_INTEREST_CENTER.getMessage());
        }

        InterestCenter interestCenter = repository.save(requestDto.toEntity());

        return RegisterInterestCenterResponseDto.from(interestCenter);
    }

}
