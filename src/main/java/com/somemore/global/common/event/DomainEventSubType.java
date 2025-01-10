package com.somemore.global.common.event;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public enum DomainEventSubType {
    CREATE_RECRUIT_BOARD("모집 글 등록"),
    VOLUNTEER_HOURS_SETTLE("봉사 시간 정산")
    ;

    private final String description;

    public static DomainEventSubType from(String value) {
        return Arrays.stream(DomainEventSubType.values())
                .filter(type -> type.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 도메인 이벤트 타입입니다: " + value));
    }
}
