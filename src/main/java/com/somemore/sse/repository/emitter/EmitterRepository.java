package com.somemore.sse.repository.emitter;

import java.util.Map;
import java.util.UUID;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface EmitterRepository {
    SseEmitter save(String emitterId, SseEmitter sseEmitter);

    Map<String, SseEmitter> findAllByReceiverId(UUID userId);

    void deleteById(String emitterId);

    void deleteAllByReceiverId(UUID userId);
}