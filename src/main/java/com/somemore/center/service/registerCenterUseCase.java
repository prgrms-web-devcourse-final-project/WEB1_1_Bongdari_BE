package com.somemore.center.service;

import com.somemore.center.domain.Center_NEW;

import java.util.UUID;

public interface registerCenterUseCase {

    Center_NEW register(UUID userId);
}
