package com.somemore.sse.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class SseEvent<T> {
    private final UUID receiverId;
    private final SseEventType type;
    private final T data;

    @Builder.Default
    private final LocalDateTime timestamp = LocalDateTime.now();

    public String getId() {
        return String.format("%s_%s", receiverId.toString(), UUID.randomUUID());
    }
}
