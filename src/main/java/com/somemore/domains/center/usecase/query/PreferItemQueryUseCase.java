package com.somemore.domains.center.usecase.query;

import com.somemore.domains.center.domain.PreferItem;
import com.somemore.domains.center.dto.response.PreferItemResponseDto;

import java.util.List;
import java.util.UUID;

public interface PreferItemQueryUseCase {
    List<PreferItemResponseDto> getPreferItemDtosByCenterId(UUID centerId);
    List<PreferItem> getPreferItemsByCenterId(UUID centerId);
}
