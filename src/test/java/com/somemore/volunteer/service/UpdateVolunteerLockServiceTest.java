package com.somemore.volunteer.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.somemore.support.IntegrationTestSupport;
import com.somemore.volunteer.domain.NEWVolunteer;
import com.somemore.volunteer.repository.NEWVolunteerJpaRepository;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

class UpdateVolunteerLockServiceTest extends IntegrationTestSupport {

    @Autowired
    private UpdateVolunteerLockService updateVolunteerLockService;

    @Autowired
    private NEWVolunteerJpaRepository volunteerRepository;

    @MockBean
    private RLock lock;

    @AfterEach
    void tearDown() {
        volunteerRepository.deleteAllInBatch();
    }

    @DisplayName("봉사자 아이디와 봉사 시간으로 봉사 스탯을 업데이트할 수 있다.")
    @Test
    void updateVolunteerStats() {
        // given
        NEWVolunteer volunteer = createVolunteer();
        volunteerRepository.save(volunteer);

        UUID id = volunteer.getId();
        int hours = 4;

        // when
        updateVolunteerLockService.updateVolunteerStats(id, hours);

        // then
        NEWVolunteer find = volunteerRepository.findById(id).orElseThrow();
        assertThat(find.getTotalVolunteerCount()).isEqualTo(1);
        assertThat(find.getTotalVolunteerHours()).isEqualTo(hours);
    }

    @DisplayName("봉사시간을 업데이트 할 수 있다.(동시성 테스트)")
    @Test
    void updateVolunteerStatsWithConcurrency() throws InterruptedException {
        // given
        NEWVolunteer volunteer = createVolunteer();
        volunteerRepository.save(volunteer);

        UUID id = volunteer.getId();
        int hours = 4;
        int threadCnt = 100;

        // 스레드 풀 크기를 줄여서 경합 감소 32 -> 16
        ExecutorService executorService = Executors.newFixedThreadPool(16);
        CountDownLatch latch = new CountDownLatch(threadCnt);

        // when
        for (int i = 0; i < threadCnt; i++) {
            executorService.submit(() -> {
                try {
                    updateVolunteerLockService.updateVolunteerStats(id, hours);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        NEWVolunteer find = volunteerRepository.findById(id).orElseThrow();
        assertThat(find.getTotalVolunteerCount()).isEqualTo(threadCnt);
        assertThat(find.getTotalVolunteerHours()).isEqualTo(hours * threadCnt);
    }

    @DisplayName("봉사활동 정보 업데이트 중 인터럽트 예외 발생")
    @Test
    void UpdateVolunteerStats_InterruptedException() throws InterruptedException {
        // given
        UUID id = UUID.randomUUID();
        int hours = 5;

        given(lock.tryLock(anyLong(), anyLong(), any()))
                .willThrow(new InterruptedException());

        // when
        // then
        assertThatThrownBy(
                () -> updateVolunteerLockService.updateVolunteerStats(id, hours))
                .isInstanceOf(RuntimeException.class);

    }

    private static NEWVolunteer createVolunteer() {
        UUID userId = UUID.randomUUID();
        return NEWVolunteer.createDefault(userId);
    }
}
