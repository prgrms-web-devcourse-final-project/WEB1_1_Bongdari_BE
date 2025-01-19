package com.somemore.domains.volunteer.service;

import com.somemore.domains.volunteer.domain.Volunteer;
import com.somemore.domains.volunteer.repository.VolunteerRepository;
import com.somemore.support.IntegrationTestSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.somemore.global.auth.oauth.domain.OAuthProvider.NAVER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

class UpdateVolunteerLockServiceTest extends IntegrationTestSupport {

    @Autowired
    private UpdateVolunteerLockService updateVolunteerLockService;

    @Autowired
    private VolunteerRepository volunteerRepository;

    @AfterEach
    void tearDown() {
        volunteerRepository.deleteAllInBatch();
    }

    @DisplayName("봉사자 아이디와 봉사 시간으로 봉사 스탯을 업데이트할 수 있다.")
    @Test
    void updateVolunteerStats() {
        // given
        Volunteer volunteer = Volunteer.createDefault(NAVER, "naver");
        volunteerRepository.save(volunteer);

        UUID id = volunteer.getId();
        int hours = 4;

        // when
        updateVolunteerLockService.updateVolunteerStats(id, hours);

        // then
        Volunteer find = volunteerRepository.findById(id).orElseThrow();
        assertThat(find.getTotalVolunteerCount()).isEqualTo(1);
        assertThat(find.getTotalVolunteerHours()).isEqualTo(hours);
    }

    @DisplayName("봉사시간을 업데이트 할 수 있다.(동시성 테스트)")
    @Test
    void updateVolunteerStatsWithConcurrency() throws InterruptedException {
        // given
        Volunteer volunteer = Volunteer.createDefault(NAVER, "naver");
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

        // 대기 시간을 충분히 늘림
        if (!latch.await(10, TimeUnit.SECONDS)) {
            fail("작업 완료 대기");
        }

        // then
        Volunteer find = volunteerRepository.findById(id).orElseThrow();
        assertThat(find.getTotalVolunteerCount()).isEqualTo(threadCnt);
        assertThat(find.getTotalVolunteerHours()).isEqualTo(hours * threadCnt);
    }
}
