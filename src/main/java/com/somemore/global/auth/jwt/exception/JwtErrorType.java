package com.somemore.global.auth.jwt.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtErrorType {
    MISSING_TOKEN("JWT 토큰이 없습니다."),
    INVALID_TOKEN("JWT 서명이 유효하지 않습니다."),
    EXPIRED_TOKEN("JWT 토큰이 만료되었습니다."),
    UNKNOWN_ERROR("알 수 없는 JWT 처리 오류가 발생했습니다.");

    private final String message;
}
