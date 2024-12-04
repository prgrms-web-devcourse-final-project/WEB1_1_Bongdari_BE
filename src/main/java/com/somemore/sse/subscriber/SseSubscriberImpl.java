package com.somemore.sse.subscriber;

import com.somemore.sse.repository.emitter.EmitterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

@RequiredArgsConstructor
@Transactional
@Component
public class SseSubscriberImpl implements SseSubscriber {

    private final EmitterRepository emitterRepository;
    private static final Long timeoutMillis = 600_000L;

    public SseEmitter subscribe(UUID userId) {
        return initEmitter(createEmitterId(userId));
    }

    private SseEmitter initEmitter(String emitterId) {
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(timeoutMillis));
        setupLifeCycle(emitter, emitterId);

        // TODO 초기화 메시지 sendNotification(emitter, emitterId, emitterId, String.format("EventStream connected [EMITTER_ID=%s]", emitterId));
        return emitter;
    }

    private String createEmitterId(UUID id) {
        return id.toString() + "_" + System.currentTimeMillis();
    }

    private void setupLifeCycle(SseEmitter emitter, String emitterId) {
        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));
    }
}
