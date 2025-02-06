package com.somemore.center.usecase;

import java.util.UUID;

public interface DeletePreferItemUseCase {

    void deletePreferItem(UUID centerId, Long preferItemId);
}
