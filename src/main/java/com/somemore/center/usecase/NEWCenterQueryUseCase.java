package com.somemore.center.usecase;

import com.somemore.center.domain.NEWCenter;
import com.somemore.center.dto.response.CenterProfileResponseDto;
import com.somemore.center.repository.record.CenterOverviewInfo;
import java.util.List;
import java.util.UUID;

public interface NEWCenterQueryUseCase {

    NEWCenter getByUserId(UUID userId);

    UUID getIdByUserId(UUID userId);

    CenterProfileResponseDto getCenterProfileById(UUID centerId);

    void validateCenterExists(UUID id);

    List<CenterOverviewInfo> getCenterOverviewsByIds(List<UUID> ids);
}
