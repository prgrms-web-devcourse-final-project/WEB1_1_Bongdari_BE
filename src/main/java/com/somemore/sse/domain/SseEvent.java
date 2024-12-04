package com.somemore.sse.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class SseEvent<T> {
    private final UUID receiverId;
    private final SseEventType type;
    private final T data;
    private final LocalDateTime timestamp = LocalDateTime.now();

    public String getId() {
        return String.format("%s_%s", receiverId.toString(), UUID.randomUUID());
    }
}
