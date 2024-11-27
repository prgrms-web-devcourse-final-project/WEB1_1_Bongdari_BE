package com.somemore.auth.jwt.domain;

import lombok.Getter;

@Getter
public enum TokenType {
    ACCESS(1000 * 60 * 30),
    REFRESH(1000 * 60 * 60 * 24 * 7),
    SIGNOUT(0);

    private final int period;

    TokenType(int period) {
        this.period = period;
    }

    public int getPeriodInSeconds() {
        return Math.toIntExact(period / 1000);
    }
}
