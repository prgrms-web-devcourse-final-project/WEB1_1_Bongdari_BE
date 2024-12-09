package com.somemore.interestcenter.usecase;

import com.somemore.interestcenter.dto.request.RegisterInterestCenterRequestDto;
import com.somemore.interestcenter.dto.response.RegisterInterestCenterResponseDto;
import java.util.UUID;

public interface RegisterInterestCenterUseCase {
    RegisterInterestCenterResponseDto registerInterestCenter(UUID volunteerId, RegisterInterestCenterRequestDto requestDto);
}
