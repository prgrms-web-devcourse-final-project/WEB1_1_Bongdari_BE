package com.somemore.domains.volunteer.domain;

import lombok.Getter;

@Getter
public enum Tier {
    RED,
    ORANGE,
    YELLOW,
    GREEN,
    BLUE,
    INDIGO,
    VIOLET,
    RAINBOW
    ;

    public static Tier getDefault() {
        return RED;
    }
}
