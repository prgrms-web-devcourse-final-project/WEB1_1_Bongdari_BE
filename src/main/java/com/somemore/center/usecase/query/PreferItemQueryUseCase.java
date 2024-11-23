package com.somemore.center.usecase.query;

import com.somemore.center.domain.PreferItem;

import java.util.List;
import java.util.UUID;

public interface PreferItemQueryUseCase {
    List<PreferItem> getPreferItemsByCenterId(UUID centerId);
}
