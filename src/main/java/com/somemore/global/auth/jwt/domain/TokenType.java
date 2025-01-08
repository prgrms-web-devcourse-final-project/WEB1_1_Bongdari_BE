package com.somemore.global.auth.jwt.domain;

import lombok.RequiredArgsConstructor;

import java.time.Duration;

@RequiredArgsConstructor
public enum TokenType {
    ACCESS(Duration.ofMinutes(30)),
    REFRESH(Duration.ofDays(7)),
    SIGN_IN(Duration.ofMinutes(1)),
    SIGN_OUT(Duration.ZERO);

    private final Duration period;

    public String getDescription() {
        return this.name() + "_TOKEN";
    }

    public int getPeriodInMillis() {
        return Math.toIntExact(period.toMillis());
    }

    public int getPeriodInSeconds() {
        return Math.toIntExact(period.getSeconds());
    }
}
