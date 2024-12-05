package com.somemore.notification.domain;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public enum NotificationType {
    NOTE_BLAH_BLAH("쪽지"),
    REVIEW_BLAH_BLAH("후기 요청"),
    VOLUNTEER_APPLY_STATUS_CHANGE("신청 상태 변경")
    ;

    private final String description;

    public static NotificationType from(String value) {
        return Arrays.stream(NotificationType.values())
                .filter(type -> type.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 타입입니다: " + value));
    }
}