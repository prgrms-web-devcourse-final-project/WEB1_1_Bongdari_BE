package com.somemore.sse.sender;

import com.somemore.sse.repository.EmitterRepository;
import com.somemore.sse.domain.SseEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Transactional
@Component
public class SseSenderImpl implements SseSender {

    private final EmitterRepository emitterRepository;

    public <T> void send(SseEvent<T> sseEvent) {
        UUID receiverId = sseEvent.getReceiverId();
        emitterRepository.findAllByReceiverId(receiverId)
                .forEach((emitterId, emitter)
                        -> sendEvent(emitterId, emitter, sseEvent));
    }

    private <T> void sendEvent(String emitterId,
                               SseEmitter emitter,
                               SseEvent<T> sseEvent) {
        try {
            emitter.send(buildEvent(sseEvent));
        } catch (IOException e) {
            emitterRepository.deleteById(emitterId);
        }
    }

    private <T> SseEmitter.SseEventBuilder buildEvent(SseEvent<T> sseEvent) {
        return SseEmitter.event()
                .id(sseEvent.getId())
                .data(sseEvent.getData());
    }
}
