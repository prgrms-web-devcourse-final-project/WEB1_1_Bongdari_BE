package com.somemore.domains.interestcenter.usecase;

import com.somemore.domains.interestcenter.dto.request.RegisterInterestCenterRequestDto;
import com.somemore.domains.interestcenter.dto.response.RegisterInterestCenterResponseDto;

import java.util.UUID;

public interface RegisterInterestCenterUseCase {
    RegisterInterestCenterResponseDto registerInterestCenter(UUID volunteerId, RegisterInterestCenterRequestDto requestDto);
}
