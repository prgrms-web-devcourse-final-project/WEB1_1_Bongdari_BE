package com.somemore.domains.recruitboard.service.validator;

import com.somemore.domains.recruitboard.domain.RecruitBoard;
import com.somemore.global.exception.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.somemore.global.exception.ExceptionMessage.INVALID_RECRUIT_BOARD_TIME;
import static com.somemore.global.exception.ExceptionMessage.UNAUTHORIZED_RECRUIT_BOARD;
import static com.somemore.support.fixture.LocalDateTimeFixture.createStartDateTime;
import static com.somemore.support.fixture.RecruitBoardFixture.createRecruitBoard;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RecruitBoardValidatorTest {

    private final RecruitBoardValidator validator = new RecruitBoardValidator();

    @DisplayName("봉사 종료 시간이 시작 시간과 같거나 빠르면, 봉사 모집글 생성 시 에러가 발생한다")
    @ParameterizedTest
    @ValueSource(longs = {0, -1})
    void createRecruitBoardWithInValidVolunteerTime(long minutesOffset) {
        // given
        LocalDateTime now = createStartDateTime();
        LocalDateTime endDateTime = now.plusMinutes(minutesOffset);

        // when & then
        assertThatThrownBy(
                () -> validator.validateRecruitBoardTime(now, endDateTime))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(INVALID_RECRUIT_BOARD_TIME.getMessage());

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
