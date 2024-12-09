package com.somemore.notification.domain;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public enum NotificationSubType {
    NOTE_BLAH_BLAH("쪽지"),
    VOLUNTEER_REVIEW_REQUEST("봉사 후기 요청"),
    VOLUNTEER_APPLY_STATUS_CHANGE("신청 상태 변경"),
    COMMENT_ADDED("댓글 대댓글 추가"),
    VOLUNTEER_APPLY("봉사 신청"),
    ;

    private final String description;

    public static NotificationSubType from(String value) {
        return Arrays.stream(NotificationSubType.values())
                .filter(type -> type.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 알림 타입입니다: " + value));
    }
}
