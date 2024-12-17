package com.somemore.global.sse.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

class EmitterRepositoryTest {

    private EmitterRepositoryImpl emitterRepository;

    @BeforeEach
    void setup() {
        // 자바 메모리 기반 repository
        emitterRepository = new EmitterRepositoryImpl();
    }

    @DisplayName("Emitter를 저장하고 반환한다.")
    @Test
    void saveEmitter() {
        // given
        String emitterId = UUID.randomUUID().toString();
        SseEmitter sseEmitter = new SseEmitter();

        // when
        SseEmitter savedEmitter = emitterRepository.save(emitterId, sseEmitter);

        // then
        assertThat(savedEmitter).isEqualTo(sseEmitter);
        assertThat(emitterRepository.findAllByReceiverId(UUID.fromString(emitterId))).hasSize(1);
    }

    @DisplayName("Receiver ID로 관련된 모든 Emitter를 조회한다.")
    @Test
    void findAllByReceiverId() {
        // given
        UUID receiverId = UUID.randomUUID();
        String emitterId1 = receiverId + "-1";
        String emitterId2 = receiverId + "-2";

        emitterRepository.save(emitterId1, new SseEmitter());
        emitterRepository.save(emitterId2, new SseEmitter());

        // when
        Map<String, SseEmitter> emitters = emitterRepository.findAllByReceiverId(receiverId);

        // then
        assertThat(emitters).hasSize(2);
        assertThat(emitters).containsKeys(emitterId1, emitterId2);
    }

    @DisplayName("Emitter ID로 특정 Emitter를 삭제한다.")
    @Test
    void deleteById() {
        // given
        String emitterId = UUID.randomUUID().toString();
        SseEmitter sseEmitter = new SseEmitter();
        emitterRepository.save(emitterId, sseEmitter);

        // when
        emitterRepository.deleteById(emitterId);

        // then
        assertThat(emitterRepository.findAllByReceiverId(UUID.fromString(emitterId))).isEmpty();
    }

    @DisplayName("Receiver ID로 관련된 모든 Emitter를 삭제한다.")
    @Test
    void deleteAllByReceiverId() {
        // given
        UUID receiverId = UUID.randomUUID();
        String emitterId1 = receiverId + "-1";
        String emitterId2 = receiverId + "-2";

        emitterRepository.save(emitterId1, new SseEmitter());
        emitterRepository.save(emitterId2, new SseEmitter());

        // when
        emitterRepository.deleteAllByReceiverId(receiverId);

        // then
        assertThat(emitterRepository.findAllByReceiverId(receiverId)).isEmpty();
    }
}
