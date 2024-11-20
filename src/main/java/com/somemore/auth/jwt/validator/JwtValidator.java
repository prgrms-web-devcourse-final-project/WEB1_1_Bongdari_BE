package com.somemore.auth.jwt.validator;

public interface JwtValidator {
    boolean validateToken(String token);
}
