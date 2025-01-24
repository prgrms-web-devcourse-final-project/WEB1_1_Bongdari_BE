package com.somemore.center.usecase;

import com.somemore.center.domain.NEWCenter;
import com.somemore.center.dto.response.CenterProfileResponseDto;

import java.util.UUID;

public interface NEWCenterQueryUseCase {

    NEWCenter getByUserId(UUID userId);

    UUID getIdByUserId(UUID userId);

    CenterProfileResponseDto getCenterProfileById(UUID centerId);
}
