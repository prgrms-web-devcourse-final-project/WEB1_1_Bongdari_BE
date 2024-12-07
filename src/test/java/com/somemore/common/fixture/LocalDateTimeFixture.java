package com.somemore.common.fixture;

import java.time.LocalDateTime;

public class LocalDateTimeFixture {

    private LocalDateTimeFixture() {
    }

    public static LocalDateTime createStartDateTime() {
        // 내일 날짜로 13:00:00으로 설정
        LocalDateTime today = LocalDateTime.now();
        return today.plusDays(1).withHour(13).withMinute(0).withSecond(0).withNano(0);
    }

    public static LocalDateTime createUpdateStartDateTime() {
        // 내일 날짜로 16:00:00으로 설정
        LocalDateTime today = LocalDateTime.now();
        return today.plusDays(1).withHour(16).withMinute(0).withSecond(0).withNano(0);
    }

    public static LocalDateTime createCurrentDateTime() {
        // 오늘 날짜로 16:00:00으로 설정
        LocalDateTime today = LocalDateTime.now();
        return today.withHour(16).withMinute(0).withSecond(0).withNano(0);
    }

}
