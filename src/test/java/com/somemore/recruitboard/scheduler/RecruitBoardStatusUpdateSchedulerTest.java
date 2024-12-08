package com.somemore.recruitboard.scheduler;

import static com.somemore.common.fixture.RecruitBoardFixture.createRecruitBoard;
import static com.somemore.recruitboard.domain.RecruitStatus.CLOSED;
import static com.somemore.recruitboard.domain.RecruitStatus.COMPLETED;
import static com.somemore.recruitboard.domain.RecruitStatus.RECRUITING;
import static org.assertj.core.api.Assertions.assertThat;

import com.somemore.IntegrationTestSupport;
import com.somemore.recruitboard.domain.RecruitBoard;
import com.somemore.recruitboard.domain.RecruitStatus;
import com.somemore.recruitboard.repository.RecruitBoardRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RecruitBoardStatusUpdateSchedulerTest extends IntegrationTestSupport {

    @Autowired
    private RecruitBoardRepository recruitBoardRepository;

    @DisplayName("스케쥴링으로 봉사 시작일 00시에 모집글 상태가 모집 완료로 변경 된다.")
    @Test
    void transitionBoardsToClosed() {
        // given
        LocalDateTime start = LocalDate.now().atStartOfDay().plusHours(1); // 시작일 00시
        LocalDateTime end = start.plusHours(2); // 종료일 02시

        List<RecruitBoard> recruitBoards = getRecruitBoards(start, end, RECRUITING);
        recruitBoardRepository.saveAll(recruitBoards);

        // when
        // then
        Awaitility.await()
                .atMost(4, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    List<RecruitBoard> updatedBoards = recruitBoardRepository.findAllByIds(
                            recruitBoards.stream().map(RecruitBoard::getId).toList());
                    assertThat(updatedBoards)
                            .allMatch(board -> board.getRecruitStatus() == CLOSED);
                });
    }

    @DisplayName("스케쥴링으로 봉사 종료일 24시에 모집글 상태가 종료로 변경 된다.")
    @Test
    void transitionBoardsToCompleted() {
        // given
        LocalDateTime start = LocalDate.now().atStartOfDay().minusHours(12);
        LocalDateTime end = start.plusHours(2);

        List<RecruitBoard> recruitBoards = getRecruitBoards(start, end, CLOSED);
        recruitBoardRepository.saveAll(recruitBoards);

        // when
        // then
        Awaitility.await()
                .atMost(4, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    List<RecruitBoard> updatedBoards = recruitBoardRepository.findAllByIds(
                            recruitBoards.stream().map(RecruitBoard::getId).toList());
                    assertThat(updatedBoards)
                            .allMatch(board -> board.getRecruitStatus() == COMPLETED);
                });
    }

    private List<RecruitBoard> getRecruitBoards(LocalDateTime start, LocalDateTime end,
            RecruitStatus status) {

        List<RecruitBoard> boards = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            RecruitBoard board = createRecruitBoard(start, end, status);
            boards.add(board);
        }

        return boards;
    }
}
