package com.somemore.sse.subscriber;

import com.somemore.IntegrationTestSupport;
import com.somemore.sse.repository.EmitterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class SseSubscriptionManagerTest extends IntegrationTestSupport {

    @Autowired
    private SseSubscriptionManagerImpl sseSubscriptionManager;

    @Autowired
    private EmitterRepository emitterRepository;

    private UUID userId;

    @BeforeEach
    void setup() {
        userId = UUID.randomUUID();
    }

    @DisplayName("사용자가 구독을 요청하면 Emitter를 생성하고 저장한다.")
    @Test
    void subscribe_createsEmitter() {
        // when
        sseSubscriptionManager.subscribe(userId);

        // then
        Map<String, SseEmitter> emitters = emitterRepository.findAllByReceiverId(userId);
        assertThat(emitters).hasSize(1);
        assertThat(emitters.keySet().iterator().next()).startsWith(userId.toString());
    }
}
