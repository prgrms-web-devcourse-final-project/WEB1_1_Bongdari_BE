package com.somemore.auth.jwt.generator;

public interface JwtGenerator {
    String generateToken(String userId, String role);
}
