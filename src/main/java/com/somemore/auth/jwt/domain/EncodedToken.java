package com.somemore.auth.jwt.domain;

public record EncodedToken(String value) {

    private final static String UNINITIALIZED = "UNINITIALIZED";

    public boolean isUninitialized() {
        return value == null
                || value.isEmpty()
                || value.equals(UNINITIALIZED);
    }

    public EncodedToken removePrefix(String prefix) {
        if (this.value.startsWith(prefix)) {
            return new EncodedToken(this.value.substring(prefix.length()));
        }
        return this;
    }

    public static EncodedToken createUninitialized() {
        return new EncodedToken("UNINITIALIZED");
    }}
