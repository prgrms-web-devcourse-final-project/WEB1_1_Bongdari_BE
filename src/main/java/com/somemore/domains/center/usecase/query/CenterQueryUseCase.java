package com.somemore.domains.center.usecase.query;

import com.somemore.domains.center.dto.response.CenterProfileResponseDto;
import com.somemore.domains.center.repository.mapper.CenterOverviewInfo;

import java.util.List;
import java.util.UUID;

public interface CenterQueryUseCase {
    CenterProfileResponseDto getCenterProfileByCenterId(UUID centerId);
    List<CenterOverviewInfo> getCenterOverviewsByIds(List<UUID> centerIds);
    void validateCenterExists(UUID centerId);
}
