package com.somemore.common.fixture;

import java.time.LocalDateTime;

public class LocalDateTimeFixture {

    private LocalDateTimeFixture() {
    }

    public static LocalDateTime createStartDateTime() {
        // 2024-11-25 T:13:00:00
        return LocalDateTime.of(2024, 11, 25, 13, 0);
    }

    public static LocalDateTime createUpdateStartDateTime() {
        // 2024-11-25 T:16:00:00
        return LocalDateTime.of(2024, 11, 25, 16, 0);
    }

}
