package com.somemore.volunteer.domain;

import lombok.Getter;

@Getter
public enum Tier {
    RED(1),
    ORANGE(2),
    YELLOW(3),
    GREEN(4),
    BLUE(5),
    INDIGO(6),
    VIOLET(7),
    RAINBOW(8);

    private final int rank;

    Tier(int rank) {
        this.rank = rank;
    }

}