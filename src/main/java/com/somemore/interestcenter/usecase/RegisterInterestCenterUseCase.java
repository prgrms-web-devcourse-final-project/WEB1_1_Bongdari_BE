package com.somemore.interestcenter.usecase;

import com.somemore.interestcenter.dto.request.RegisterInterestCenterRequestDto;
import com.somemore.interestcenter.dto.response.RegisterInterestCenterResponseDto;

public interface RegisterInterestCenterUseCase {
    RegisterInterestCenterResponseDto registerInterestCenter(RegisterInterestCenterRequestDto requestDto);
}
