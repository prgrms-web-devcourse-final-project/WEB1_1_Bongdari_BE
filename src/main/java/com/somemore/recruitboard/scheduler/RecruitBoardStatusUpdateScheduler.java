package com.somemore.recruitboard.scheduler;

import static com.somemore.recruitboard.domain.RecruitStatus.CLOSED;
import static com.somemore.recruitboard.domain.RecruitStatus.RECRUITING;

import com.somemore.recruitboard.domain.RecruitBoard;
import com.somemore.recruitboard.repository.RecruitBoardRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Component
public class RecruitBoardStatusUpdateScheduler {

    private final RecruitBoardRepository recruitBoardRepository;

    @Scheduled(cron = "${spring.schedules.cron.updateBoardsToClosed}")
    public void transitionBoardsToClosed() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime startOfNextDay = LocalDate.now().plusDays(1).atStartOfDay();

        List<RecruitBoard> boards = recruitBoardRepository.findByStartDateTimeBetweenAndStatus(
                startOfDay, startOfNextDay, RECRUITING);

        boards.forEach(RecruitBoard::markAsClosed);

        recruitBoardRepository.saveAll(boards);
    }

    @Scheduled(cron = "${spring.schedules.cron.updateBoardsToCompleted}")
    public void transitionBoardsToCompleted() {
        LocalDateTime now = LocalDateTime.now();

        List<RecruitBoard> boards = recruitBoardRepository.findByEndDateTimeBeforeAndStatus(
                now, CLOSED);

        boards.forEach(RecruitBoard::markAsCompleted);

        recruitBoardRepository.saveAll(boards);
    }
}

