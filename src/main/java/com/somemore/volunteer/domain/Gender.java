package com.somemore.volunteer.domain;

import lombok.Getter;

@Getter
public enum Gender {
    Male("M"),
    Female("F"),
    Undefined("U");

    private final String code;

    Gender(String code) {
        this.code = code;
    }

    public static Gender from(String code) {
        for (Gender gender : Gender.values()) {
            if (gender.code.equalsIgnoreCase(code)) {
                return gender;
            }
        }
        return Undefined;
    }
}