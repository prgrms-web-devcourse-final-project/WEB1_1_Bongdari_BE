package com.somemore.sse.usecase;

import com.somemore.sse.domain.SseEvent;

import java.util.UUID;

public interface SseUseCase {

    void subscribe(UUID userId);

    <T> void send(SseEvent<T> event);
}
