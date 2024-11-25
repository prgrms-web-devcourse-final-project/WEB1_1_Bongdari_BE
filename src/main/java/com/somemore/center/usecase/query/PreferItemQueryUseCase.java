package com.somemore.center.usecase.query;

import com.somemore.center.domain.PreferItem;
import com.somemore.center.dto.response.PreferItemResponseDto;

import java.util.List;
import java.util.UUID;

public interface PreferItemQueryUseCase {
    List<PreferItemResponseDto> getPreferItemDtosByCenterId(UUID centerId);
    List<PreferItem> getPreferItemsByCenterId(UUID centerId);
}
