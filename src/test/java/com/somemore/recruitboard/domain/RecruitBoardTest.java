package com.somemore.recruitboard.domain;

import static com.somemore.common.fixture.LocalDateTimeFixture.createStartDateTime;
import static com.somemore.common.fixture.LocalDateTimeFixture.createUpdateStartDateTime;
import static com.somemore.recruitboard.domain.RecruitStatus.RECRUITING;
import static com.somemore.recruitboard.domain.VolunteerType.OTHER;
import static org.assertj.core.api.Assertions.assertThat;

import com.somemore.recruitboard.dto.request.RecruitBoardUpdateRequestDto;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        UUID centerId = UUID.randomUUID();
        LocalDateTime startDateTime = createStartDateTime();
        LocalDateTime endDateTime = startDateTime.plusHours(hours);

        RecruitBoard board = createRecruitBoard(centerId, startDateTime, endDateTime);

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
            .volunteerType(OTHER)
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
        VolunteerInfo volunteerInfo = board.getVolunteerInfo();
        assertThat(volunteerInfo.getRegion()).isEqualTo(updateRegion);
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
        boolean isNotWriter = recruitBoard.isNotWriter(wrongId);

        // then
        assertThat(isNotWriter).isTrue();
    }


    private static RecruitBoard createRecruitBoard(UUID centerId) {
        LocalDateTime startDateTime = createStartDateTime();
        LocalDateTime endDateTime = startDateTime.plusHours(1);

        return createRecruitBoard(centerId, startDateTime, endDateTime);
    }

    private static RecruitBoard createRecruitBoard(UUID centerId, LocalDateTime startDateTime,
        LocalDateTime endDateTime) {

        VolunteerInfo volunteerInfo = VolunteerInfo.builder()
            .region("경기")
            .recruitmentCount(1)
            .volunteerStartDateTime(startDateTime)
            .volunteerEndDateTime(endDateTime)
            .volunteerType(OTHER)
            .admitted(true)
            .build();

        return RecruitBoard.builder()
            .centerId(centerId)
            .locationId(1L)
            .title("봉사모집제목")
            .content("봉사모집내용")
            .imgUrl("https://image.domain.com/links")
            .volunteerInfo(volunteerInfo)
            .build();
    }

}