package com.somemore.domains.volunteer.domain;

import lombok.Getter;

@Getter
public enum Gender {
    MALE("M"),
    FEMALE("F"),
    UNDEFINED("U");

    private final String code;

    Gender(String code) {
        this.code = code;
    }

    public static Gender getDefault() {
        return UNDEFINED;
    }

    public static Gender from(String code) {
        for (Gender gender : Gender.values()) {
            if (gender.code.equalsIgnoreCase(code)) {
                return gender;
            }
        }
        return UNDEFINED;
    }
}
