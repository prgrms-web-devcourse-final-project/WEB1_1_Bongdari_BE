package com.somemore.center.usecase;

import com.somemore.center.domain.NEWCenter;

import java.util.UUID;

public interface NEWCenterQueryUseCase {

    NEWCenter getByUserId(UUID userId);

    UUID getIdByUserId(UUID userId);
}
