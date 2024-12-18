package com.somemore.domains.center.usecase.command;

import java.util.UUID;

public interface DeletePreferItemUseCase {
    void deletePreferItem(UUID centerId, Long preferItemId);
}
