package com.somemore.center.usecase;

import com.somemore.center.domain.Center_NEW;

import java.util.UUID;

public interface RegisterCenterUseCase {

    Center_NEW register(UUID userId);
}
