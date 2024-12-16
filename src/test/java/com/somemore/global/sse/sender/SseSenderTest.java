package com.somemore.global.sse.sender;

import com.somemore.support.IntegrationTestSupport;
import com.somemore.global.sse.domain.SseEvent;
import com.somemore.global.sse.domain.SseEventType;
import com.somemore.global.sse.repository.EmitterRepository;
import com.somemore.global.sse.sender.SseSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class SseSenderTest extends IntegrationTestSupport {

    @Autowired
    private EmitterRepository emitterRepository;

    @Autowired
    private SseSender sseSender;

    private UUID receiverId;

    @BeforeEach
    void setup() {
        receiverId = UUID.randomUUID();
    }

    @DisplayName("SSE 이벤트를 성공적으로 전송한다.")
    @Test
    void sendEventSuccessfully() {
        // given
        SseEmitter sseEmitter = new SseEmitter();
        emitterRepository.save(receiverId.toString(), sseEmitter);

        SseEvent<String> sseEvent = buildSseEvent();

        // when
        sseSender.send(sseEvent);

        // then
        assertThat(emitterRepository.findAllByReceiverId(receiverId)).isNotEmpty();
    }



    @DisplayName("SSE 전송 중 IOException 발생 시 emitter를 제거한다.")
    @Test
    void removeEmitterOnIOException() {
        // given
        String emitterId = receiverId.toString();
        SseEmitter sseEmitter = new SseEmitter() {
            @Override
            public void send(SseEventBuilder builder) throws IOException {
                throw new IOException("Forced Exception for TEST");
            }
        };
        emitterRepository.save(emitterId, sseEmitter);

        SseEvent<String> sseEvent = buildSseEvent();

        // when
        sseSender.send(sseEvent);

        // then
        assertThat(emitterRepository.findAllByReceiverId(receiverId)).isEmpty();
    }

    @DisplayName("다수의 Emitter에게 이벤트를 전송한다.")
    @Test
    void sendEventToMultipleEmitters() {
        // given
        String firstEmitterId = receiverId + "-1";
        String secondEmitterId = receiverId + "-2";

        TestableSseEmitter firstEmitter = new TestableSseEmitter();
        TestableSseEmitter secondEmitter = new TestableSseEmitter();

        emitterRepository.save(firstEmitterId, firstEmitter);
        emitterRepository.save(secondEmitterId, secondEmitter);

        SseEvent<String> sseEvent = buildSseEvent();

        // when
        sseSender.send(sseEvent);

        // then
        assertThat(firstEmitter.isEventSent()).isTrue();
        assertThat(secondEmitter.isEventSent()).isTrue();
        assertThat(emitterRepository.findAllByReceiverId(receiverId)).hasSize(2);
    }

    static class TestableSseEmitter extends SseEmitter {
        private boolean eventSent = false;

        @Override
        public void send(SseEventBuilder builder) throws IOException {
            this.eventSent = true;
        }

        public boolean isEventSent() {
            return eventSent;
        }
    }

    private SseEvent<String> buildSseEvent() {
        return SseEvent.<String>builder()
                .receiverId(receiverId)
                .type(SseEventType.NOTIFICATION)
                .data("Test Data")
                .build();
    }
}
