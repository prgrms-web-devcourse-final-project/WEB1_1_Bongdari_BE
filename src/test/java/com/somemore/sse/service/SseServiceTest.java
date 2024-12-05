package com.somemore.sse.service;

import com.somemore.IntegrationTestSupport;
import com.somemore.sse.repository.EmitterRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class SseServiceTest extends IntegrationTestSupport {

    @Autowired
    private SseService sseService;

    @Autowired
    private EmitterRepository emitterRepository;

    @Test
    @DisplayName("subscribe 호출 시 Emitter가 등록된다")
    void subscribe() {
        // given
        UUID userId = UUID.randomUUID();

        // when
        sseService.subscribe(userId);

        // then
        assertThat(emitterRepository.findAllByReceiverId(userId)).isNotEmpty();
    }
}