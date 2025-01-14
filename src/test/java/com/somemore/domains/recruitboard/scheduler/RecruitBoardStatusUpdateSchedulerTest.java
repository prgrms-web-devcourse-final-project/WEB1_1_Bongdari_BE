package com.somemore.domains.recruitboard.scheduler;

import static com.somemore.domains.recruitboard.domain.RecruitStatus.CLOSED;
import static com.somemore.domains.recruitboard.domain.RecruitStatus.COMPLETED;
import static com.somemore.domains.recruitboard.domain.RecruitStatus.RECRUITING;
import static com.somemore.domains.recruitboard.domain.VolunteerCategory.OTHER;
import static org.assertj.core.api.Assertions.assertThat;

import com.somemore.domains.recruitboard.domain.RecruitBoard;
import com.somemore.domains.recruitboard.domain.RecruitStatus;
import com.somemore.domains.recruitboard.domain.RecruitmentInfo;
import com.somemore.domains.recruitboard.repository.RecruitBoardRepository;
import com.somemore.support.IntegrationTestSupport;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class RecruitBoardStatusUpdateSchedulerTest extends IntegrationTestSupport {

    @Autowired
    private RecruitBoardRepository recruitBoardRepository;

    @Autowired
    private RecruitBoardStatusUpdateScheduler scheduler;

    @Autowired
    private EntityManager em;

    @DisplayName("봉사 시작일에 해당하는 모집글 상태를 CLOSED 변경")
    @Test
    void updateRecruitBoardStatusToClosed() {
        // given
        LocalDateTime today = LocalDate.now().atStartOfDay(); // today 00:00:00
        LocalDateTime startDateTime = today.plusHours(12); // today 12:00:00
        LocalDateTime endDateTime = startDateTime.plusHours(2); // today 14:00:00

        RecruitBoard boardOne = createRecruitBoard(startDateTime, endDateTime, RECRUITING);
        RecruitBoard boardTwo = createRecruitBoard(startDateTime, endDateTime, RECRUITING);
        recruitBoardRepository.saveAll(List.of(boardOne, boardTwo));

        // when
        scheduler.updateRecruitBoardStatusToClosed();
        em.clear();

        // then
        RecruitBoard one = recruitBoardRepository.findById(boardOne.getId()).orElseThrow();
        RecruitBoard two = recruitBoardRepository.findById(boardTwo.getId()).orElseThrow();
        assertThat(one.getRecruitStatus()).isEqualTo(CLOSED);
        assertThat(two.getRecruitStatus()).isEqualTo(CLOSED);
    }

    @DisplayName("봉사 종료일 기준으로 모집완료 상태 게시글을 종료로 변경한다")
    @Test
    void updateRecruitBoardStatusToCompleted() {
        // given
        LocalDateTime now = LocalDate.now().atStartOfDay(); // today 00:00:00
        LocalDateTime yesterday = now.minusDays(1); // yesterday 00:00:00
        LocalDateTime startDateTime = yesterday.plusHours(12); // yesterday 12:00:00
        LocalDateTime endDateTime = startDateTime.plusHours(2); // yesterday 14:00:00

        RecruitBoard boardOne = createRecruitBoard(startDateTime, endDateTime, CLOSED);
        RecruitBoard boardTwo = createRecruitBoard(startDateTime, endDateTime, CLOSED);
        recruitBoardRepository.saveAll(List.of(boardOne, boardTwo));

        // when
        scheduler.updateRecruitBoardStatusToCompleted();
        em.clear();

        // then
        RecruitBoard one = recruitBoardRepository.findById(boardOne.getId()).orElseThrow();
        RecruitBoard two = recruitBoardRepository.findById(boardTwo.getId()).orElseThrow();
        assertThat(one.getRecruitStatus()).isEqualTo(COMPLETED);
        assertThat(two.getRecruitStatus()).isEqualTo(COMPLETED);
    }

    public static RecruitBoard createRecruitBoard(LocalDateTime startTime, LocalDateTime endTime,
            RecruitStatus status) {

        RecruitmentInfo recruitmentInfo = RecruitmentInfo.builder()
                .region("지역")
                .recruitmentCount(1)
                .volunteerStartDateTime(startTime)
                .volunteerEndDateTime(endTime)
                .volunteerHours(10)
                .volunteerCategory(OTHER)
                .admitted(true)
                .build();

        return RecruitBoard.builder()
                .centerId(UUID.randomUUID())
                .locationId(1L)
                .title("모집글 제목")
                .content("모집글 내용")
                .imgUrl("이미지 링크")
                .recruitmentInfo(recruitmentInfo)
                .status(status)
                .build();
    }
}
