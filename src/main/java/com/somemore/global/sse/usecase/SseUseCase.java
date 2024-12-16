package com.somemore.global.sse.usecase;

import com.somemore.global.sse.domain.SseEvent;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

public interface SseUseCase {

    SseEmitter subscribe(UUID userId);

    <T> void send(SseEvent<T> event);
}
