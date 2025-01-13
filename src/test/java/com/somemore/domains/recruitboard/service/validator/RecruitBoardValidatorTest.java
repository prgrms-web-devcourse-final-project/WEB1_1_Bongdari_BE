package com.somemore.domains.recruitboard.service.validator;

import static com.somemore.global.exception.ExceptionMessage.INVALID_RECRUIT_BOARD_TIME;
import static com.somemore.global.exception.ExceptionMessage.INVALID_RECRUIT_BOARD_TIME_UPDATE;
import static com.somemore.global.exception.ExceptionMessage.UNAUTHORIZED_RECRUIT_BOARD;
import static com.somemore.support.fixture.RecruitBoardFixture.createRecruitBoard;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.somemore.domains.recruitboard.domain.RecruitBoard;
import com.somemore.global.exception.BadRequestException;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class RecruitBoardValidatorTest {

    private final RecruitBoardValidator validator = new RecruitBoardValidator();

    @DisplayName("봉사 종료 시간이 시작 시간과 같거나 빠르면, 봉사 모집글 생성 시 에러가 발생한다")
    @ParameterizedTest
    @ValueSource(longs = {0, -1})
    void validateRecruitBoardTimeWhenNotValid(long minutesOffset) {
        // given
        LocalDateTime now = LocalDateTime.of(2025, 1, 1, 10, 0);
        LocalDateTime endDateTime = now.plusMinutes(minutesOffset);

        // when
        // then
        assertThatThrownBy(
                () -> validator.validateRecruitBoardTime(now, endDateTime))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(INVALID_RECRUIT_BOARD_TIME.getMessage());

    }

    @Test
    @DisplayName("StartDateTime이 createdAt + 1일 이후일 경우 예외 없이 통과")
    void validateUpdateRecruitBoardTime() {
        // given
        LocalDateTime createdAt = LocalDateTime.of(2025, 1, 1, 10, 0); // 2025-01-01 10:00:00
        LocalDateTime start = LocalDateTime.of(2025, 1, 2, 10, 0); // 2025-01-02 10:00:00
        LocalDateTime end = LocalDateTime.of(2025, 1, 3, 10, 0); // 2025-01-03 10:00:00

        // when
        // then
        assertDoesNotThrow(() -> validator.validateUpdateRecruitBoardTime(createdAt, start, end));
    }

    @Test
    @DisplayName("StartDateTime이 createdAt + 1일 이전일 경우 예외 발생")
    void validateUpdateRecruitBoardTimeWhenNotValid() {
        // given
        LocalDateTime createdAt = LocalDateTime.of(2025, 1, 2, 10, 0); // 2025-1-1 10:00:00
        LocalDateTime start = LocalDateTime.of(2025, 1, 2, 12, 0); // 2025-1-2 12:00:00
        LocalDateTime end = LocalDateTime.of(2025, 1, 2, 14, 0); // 2025-1-2 14:00:00

        // when
        // then
        assertThatThrownBy(
                () -> validator.validateUpdateRecruitBoardTime(createdAt, start, end))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(INVALID_RECRUIT_BOARD_TIME_UPDATE.getMessage());

    }

    @DisplayName("모집글 작성자가 아닌 경우 에러가 발생한다")
    @Test
    void validateWriter() {
        // given
        UUID wrongCenterId = UUID.randomUUID();
        RecruitBoard board = createRecruitBoard(UUID.randomUUID());

        // when
        // then
        assertThatThrownBy(
                () -> validator.validateWriter(board, wrongCenterId))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(UNAUTHORIZED_RECRUIT_BOARD.getMessage());
    }

}
