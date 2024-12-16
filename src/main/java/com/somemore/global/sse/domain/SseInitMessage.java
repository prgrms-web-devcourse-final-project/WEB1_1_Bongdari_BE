package com.somemore.global.sse.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public record SseInitMessage(String message,
                             LocalDateTime createdAt) {

    public static SseEvent<SseInitMessage> createInitEvent(UUID userId) {
        SseInitMessage initMessage = new SseInitMessage(
                "SSE 연결이 성공적으로 설정되었습니다.",
                LocalDateTime.now()
        );

        return SseEvent.<SseInitMessage>builder()
                .receiverId(userId)
                .type(SseEventType.INITIALIZATION)
                .data(initMessage)
                .build();
    }
}
