package com.somemore.notification.sse.service;

import com.somemore.notification.repository.emitter.EmitterRepository;
import com.somemore.notification.sse.domain.Event;
import com.somemore.notification.sse.usecase.SseSender;
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

    public <T> void send(Event<T> event) {
        UUID receiverId = event.getReceiverId();
        emitterRepository.findAllByReceiverId(receiverId)
                .forEach((emitterId, emitter) -> sendEvent(emitterId, emitter, event));
    }

    private <T> void sendEvent(String emitterId,
                               SseEmitter emitter,
                               Event<T> event) {
        try {
            emitter.send(buildEvent(event));
        } catch (IOException e) {
            emitterRepository.deleteById(emitterId);
        }
    }

    private <T> SseEmitter.SseEventBuilder buildEvent(Event<T> event) {
        return SseEmitter.event()
                .id(event.getId())
                .data(event.getData());
    }
}
