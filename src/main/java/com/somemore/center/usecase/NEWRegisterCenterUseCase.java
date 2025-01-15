package com.somemore.center.usecase;

import com.somemore.center.domain.NEWCenter;

import java.util.UUID;

public interface NEWRegisterCenterUseCase {

    NEWCenter register(UUID userId);
}
