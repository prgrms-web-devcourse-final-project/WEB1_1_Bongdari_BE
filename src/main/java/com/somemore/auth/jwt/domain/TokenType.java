package com.somemore.auth.jwt.domain;

import lombok.Getter;

@Getter
public enum TokenType {
    ACCESS(1000 * 60 * 30),
    REFRESH(1000 * 60 * 60 * 24 * 7);

    private final long period;

    TokenType(long period) {
        this.period = period;
    }

    public int getPeriodInSeconds() {
        return Math.toIntExact(period / 1000);
    }
}