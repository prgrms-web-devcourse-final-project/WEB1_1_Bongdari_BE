package com.somemore.global.sse.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.somemore.global.sse.repository.EmitterRepository;
import com.somemore.support.IntegrationTestSupport;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
