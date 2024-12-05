package com.somemore.sse.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SseEventType {
    INITIALIZATION("초기화"),
    NOTIFICATION("알림")
    ;

    private final String description;
}
