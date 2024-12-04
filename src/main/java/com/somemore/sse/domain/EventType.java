package com.somemore.sse.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventType {
    NOTIFICATION("알림")
    ;

    private final String description;
}