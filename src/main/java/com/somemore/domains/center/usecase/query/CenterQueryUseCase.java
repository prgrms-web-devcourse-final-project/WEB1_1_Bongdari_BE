package com.somemore.domains.center.usecase.query;

import java.util.UUID;

public interface CenterQueryUseCase {

    void validateCenterExists(UUID centerId);

    String getNameById(UUID id);
}
