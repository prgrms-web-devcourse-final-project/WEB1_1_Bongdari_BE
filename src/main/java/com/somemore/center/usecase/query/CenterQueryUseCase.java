package com.somemore.center.usecase.query;

import com.somemore.center.repository.mapper.CenterOverviewInfo;
import com.somemore.center.dto.response.CenterProfileResponseDto;

import java.util.List;
import java.util.UUID;

public interface CenterQueryUseCase {
    CenterProfileResponseDto getCenterProfileByCenterId(UUID centerId);
    List<CenterOverviewInfo> getCenterOverviewsByIds(List<UUID> centerIds);
    void validateCenterExists(UUID centerId);
}
