package com.somemore.global.auth.jwt.domain;

public record EncodedToken(String value) {

    private static final String UNINITIALIZED = "UNINITIALIZED";
    private static final String PREFIX = "Bearer ";

    public static EncodedToken from(String value) {
        return new EncodedToken(value);
    }

    public String getValueWithPrefix() {
        return PREFIX + this.value;
    }

    public boolean isUninitialized() {
        return value == null
                || value.isEmpty()
                || value.equals(UNINITIALIZED);
    }

    public EncodedToken removePrefix() {
        if (this.value.startsWith(PREFIX)) {
            return new EncodedToken(this.value.substring(PREFIX.length()));
        }
        return this;
    }

    public static EncodedToken createUninitialized() {
        return new EncodedToken(UNINITIALIZED);
    }
}
