package com.somemore.user.usecase;

import java.util.UUID;

public interface ValidateBasicInfoUseCase {

    boolean isBasicInfoComplete(UUID userId);
}
