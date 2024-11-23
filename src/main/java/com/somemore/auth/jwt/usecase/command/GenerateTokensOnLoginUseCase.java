package com.somemore.auth.jwt.usecase.command;

import com.somemore.auth.jwt.domain.EncodedToken;

import java.util.UUID;

public interface GenerateTokensOnLoginUseCase {
    EncodedToken saveRefreshTokenAndReturnAccessToken(UUID volunteerId);
}
