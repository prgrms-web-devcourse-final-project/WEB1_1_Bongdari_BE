package com.somemore.center.usecase.query;

import com.somemore.center.dto.response.CenterProfileResponseDto;

import java.util.UUID;

public interface CenterQueryUseCase {

    CenterProfileResponseDto getCenterProfileByCenterId(UUID centerId);
    void validateCenterExists(UUID centerId);
}
