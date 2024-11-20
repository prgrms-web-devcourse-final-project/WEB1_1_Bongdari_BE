package com.somemore.auth.jwt.usecase;

public interface JwtUseCase {
    String generateToken(String userId, String role);

    boolean verifyToken(String token);

    String getClaimByKey(String token, String key);
}