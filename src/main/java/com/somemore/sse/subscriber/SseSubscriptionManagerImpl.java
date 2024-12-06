package com.somemore.sse.subscriber;

import com.somemore.sse.repository.EmitterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

@RequiredArgsConstructor
@Transactional
@Component
public class SseSubscriptionManagerImpl implements SseSubscriptionManager {

    private final EmitterRepository emitterRepository;
    private static final Long TIMEOUT_MILLI_SEC = 600_000L;

    public void subscribe(UUID userId) {
        initEmitter(createEmitterId(userId));
    }

    private void initEmitter(String emitterId) {
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(TIMEOUT_MILLI_SEC));
        setupLifeCycle(emitter, emitterId);
    }

    private String createEmitterId(UUID id) {
        return id.toString() + "_" + System.currentTimeMillis();
    }

    private void setupLifeCycle(SseEmitter emitter, String emitterId) {
        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));
    }
}
