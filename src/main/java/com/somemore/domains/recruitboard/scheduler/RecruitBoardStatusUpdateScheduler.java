package com.somemore.domains.recruitboard.scheduler;

import com.somemore.domains.recruitboard.repository.RecruitBoardRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Component
public class RecruitBoardStatusUpdateScheduler {

    private final RecruitBoardRepository recruitBoardRepository;
    private final static String RECRUIT_BOARD_UPDATE_CRON = "0 0 0 * * ?";

    @Retryable(
            retryFor = Exception.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    @Scheduled(cron = RECRUIT_BOARD_UPDATE_CRON)
    public void updateRecruitBoardStatusToClosed() {
        log.info("봉사 시작일에 해당하는 모집글 상태를 CLOSED로 변경하는 작업 시작");
        LocalDateTime today = LocalDate.now().atStartOfDay();
        LocalDateTime tomorrow = today.plusDays(1);

        try {
            long updatedCount = recruitBoardRepository.updateRecruitingToClosedByStartDate(
                    today, tomorrow);
            log.info("총 {}개의 모집글 상태를 CLOSED로 변경 완료", updatedCount);
        } catch (Exception e) {
            log.error("봉사 시작일에 해당하는 모집글 상태를 CLOSED로 변경하는 중 오류 발생", e);
            throw e;
        }
    }

    @Retryable(
            retryFor = Exception.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    @Scheduled(cron = RECRUIT_BOARD_UPDATE_CRON)
    public void updateRecruitBoardStatusToCompleted() {
        log.info("봉사 종료일이 지난 모집글 상태를 COMPLETED로 변경하는 작업 시작");
        LocalDateTime today = LocalDate.now().atStartOfDay();
        LocalDateTime yesterday = today.minusDays(1);

        try {
            long updatedCount = recruitBoardRepository.updateClosedToCompletedByEndDate(yesterday, today);
            log.info("총 {}개의 모집글 상태를 COMPLETED로 변경 완료", updatedCount);
        } catch (Exception e) {
            log.error("봉사 종료일이 지난 모집글 상태를 COMPLETED로 변경하는 중 오류 발생", e);
            throw e;
        }
    }

}
