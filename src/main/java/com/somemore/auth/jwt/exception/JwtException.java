package com.somemore.auth.jwt.exception;

import lombok.Getter;

@Getter
public class JwtException extends RuntimeException {
    private final JwtErrorType errorType;

    public JwtException(JwtErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }

}
