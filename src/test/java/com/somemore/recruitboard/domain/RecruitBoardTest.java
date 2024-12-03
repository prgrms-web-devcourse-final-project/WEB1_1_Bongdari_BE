package com.somemore.recruitboard.domain;

import static com.somemore.common.fixture.LocalDateTimeFixture.createCurrentDateTime;
import static com.somemore.common.fixture.LocalDateTimeFixture.createStartDateTime;
import static com.somemore.common.fixture.LocalDateTimeFixture.createUpdateStartDateTime;
import static com.somemore.common.fixture.RecruitBoardFixture.createCloseRecruitBoard;
import static com.somemore.common.fixture.RecruitBoardFixture.createRecruitBoard;
import static com.somemore.recruitboard.domain.RecruitStatus.CLOSED;
import static com.somemore.recruitboard.domain.RecruitStatus.COMPLETED;
import static com.somemore.recruitboard.domain.RecruitStatus.RECRUITING;
import static com.somemore.recruitboard.domain.VolunteerCategory.OTHER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.somemore.recruitboard.dto.request.RecruitBoardUpdateRequestDto;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class RecruitBoardTest {

    @DisplayName("봉사 모집글 생성시 모집상태는 모집중이다")
    @Test
    void createRecruitBoardWithDefaultStatus() {
        // given
        UUID centerId = UUID.randomUUID();
        RecruitBoard board = createRecruitBoard(centerId);

        // when
        RecruitStatus recruitStatus = board.getRecruitStatus();

        // then
        assertThat(board.getCenterId()).isEqualTo(centerId);
        assertThat(recruitStatus).isEqualTo(RECRUITING);
    }

    @DisplayName("봉사 시간을 계산할 수 있다")
    @Test
    void testCalculateVolunteerTime() {
        // given
        int hours = 3;
        LocalDateTime startDateTime = createStartDateTime();
        LocalDateTime endDateTime = startDateTime.plusHours(hours);

        RecruitBoard board = createRecruitBoard(startDateTime, endDateTime);

        // when
        LocalTime volunteerTime = board.getVolunteerHours();

        // then
        assertThat(volunteerTime).isEqualTo(LocalTime.of(hours, 0));
    }

    @DisplayName("봉사 모집글을 업데이트 할 수 있다")
    @Test
    void updateRecruitBoard() {
        // given
        UUID centerId = UUID.randomUUID();
        RecruitBoard board = createRecruitBoard(centerId);
        String imgUrl = "https://image.domain.com/updates";
        LocalDateTime startDateTime = createUpdateStartDateTime();
        LocalDateTime endDateTime = startDateTime.plusHours(2);

        RecruitBoardUpdateRequestDto dto = RecruitBoardUpdateRequestDto.builder()
                .title("봉사 모집글 작성 수정")
                .content("봉사 하실분을 모집합니다. 수정 <br>")
                .recruitmentCount(10)
                .volunteerStartDateTime(startDateTime)
                .volunteerEndDateTime(endDateTime)
                .volunteerCategory(OTHER)
                .admitted(true).build();

        // when
        board.updateWith(dto, imgUrl);

        // then
        assertThat(board.getTitle()).isEqualTo(dto.title());
        assertThat(board.getContent()).isEqualTo(dto.content());
        assertThat(board.getImgUrl()).isEqualTo(imgUrl);
    }

    @DisplayName("봉사 활동 지역을 수정할 수 있다.")
    @Test
    void updateWithRegion() {
        // given
        UUID centerId = UUID.randomUUID();
        RecruitBoard board = createRecruitBoard(centerId);
        String updateRegion = "새로운지역";

        // when
        board.updateWith(updateRegion);

        // then
        RecruitmentInfo recruitmentInfo = board.getRecruitmentInfo();
        assertThat(recruitmentInfo.getRegion()).isEqualTo(updateRegion);
    }

    @DisplayName("올바른 기관 식별 값이 주어지면 작성자인지 확인할 수 있다")
    @Test
    void isWriterWithCorrectCenterId() {
        // given
        UUID centerId = UUID.randomUUID();
        RecruitBoard recruitBoard = createRecruitBoard(centerId);

        // when
        boolean isWriter = recruitBoard.isWriter(centerId);

        // then
        assertThat(isWriter).isTrue();
    }

    @DisplayName("잘못된 기관 식별 값이 주어지면 잘못된 작성자인 확인할 수있다.")
    @Test
    void isNotWriterWithWrongCenterId() {
        UUID centerId = UUID.randomUUID();
        UUID wrongId = UUID.randomUUID();
        RecruitBoard recruitBoard = createRecruitBoard(centerId);

        // when
        boolean isWriter = recruitBoard.isWriter(wrongId);

        // then
        assertThat(isWriter).isFalse();
    }

    @DisplayName("모집글 상태를 모집중에서 모집 마감으로 변경할 수 있다")
    @Test
    void changeStatusFromRecruitingToClose() {
        // given
        UUID centerId = UUID.randomUUID();
        RecruitBoard recruitBoard = createRecruitBoard(centerId);
        RecruitStatus newStatus = CLOSED;
        LocalDateTime currentDateTime = createCurrentDateTime();

        // when
        recruitBoard.changeRecruitStatus(newStatus, currentDateTime);

        // then
        assertThat(recruitBoard.getRecruitStatus()).isEqualTo(newStatus);
    }

    @DisplayName("모집글 상태를 모집마감에서 모집중으로 변경할 수 있다")
    @Test
    void changeStatusFromCloseToRecruiting() {
        // given
        UUID centerId = UUID.randomUUID();
        RecruitBoard recruitBoard = createRecruitBoard(centerId);
        LocalDateTime currentDateTime = createCurrentDateTime();
        recruitBoard.changeRecruitStatus(CLOSED, currentDateTime);
        RecruitStatus newStatus = RECRUITING;

        // when
        recruitBoard.changeRecruitStatus(newStatus, currentDateTime);

        // then
        assertThat(recruitBoard.getRecruitStatus()).isEqualTo(newStatus);
    }

    @DisplayName("모집글 상태는 마감으로 변경할 수 없다")
    @Test
    void changeStatusWhenInvalidStatus() {
        // given
        UUID centerId = UUID.randomUUID();
        RecruitBoard recruitBoard = createRecruitBoard(centerId);
        LocalDateTime currentDateTime = createCurrentDateTime();

        // when & then
        assertThatThrownBy(() -> recruitBoard.changeRecruitStatus(COMPLETED, currentDateTime))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("봉사 시작일 자정 이후 모집 상태를 변경할 경우 에러가 발생한다")
    @ParameterizedTest
    @ValueSource(longs = {0, 1})
    void changeStatusWhenDeadLineAfter(Long secondsOffset) {
        // given
        UUID centerId = UUID.randomUUID();
        RecruitBoard recruitBoard = createRecruitBoard(centerId);
        LocalDateTime deadLineDateTime = recruitBoard.getRecruitmentInfo()
                .getVolunteerStartDateTime().toLocalDate().atStartOfDay();
        LocalDateTime currentDateTime = deadLineDateTime.plusSeconds(secondsOffset);

        // when
        // then
        assertThatThrownBy(
                () -> recruitBoard.changeRecruitStatus(CLOSED, currentDateTime)
        ).isInstanceOf(IllegalStateException.class);

    }

    @DisplayName("모집중일 경우 지원이 가능하다")
    @Test
    void isApplicationOpen() {
        // given
        RecruitBoard board = createRecruitBoard();

        // when
        boolean result = board.isApplicationOpen();

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("모집중이 아닐 경우 지원이 불가능하다")
    @Test
    void isApplicationOpenWhenNotRECRUTING() {
        // given
        RecruitBoard board = createCloseRecruitBoard();

        // when
        boolean result = board.isApplicationOpen();

        // then
        assertThat(result).isFalse();
    }
}
