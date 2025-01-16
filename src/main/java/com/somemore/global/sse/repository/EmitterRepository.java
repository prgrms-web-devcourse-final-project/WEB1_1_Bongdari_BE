package com.somemore.global.sse.repository;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.UUID;

public interface EmitterRepository {
    SseEmitter save(String emitterId, SseEmitter sseEmitter);

    Map<String, SseEmitter> findAllByReceiverId(UUID userId);

    void deleteById(String emitterId);

    void deleteAllByReceiverId(UUID userId);
}
