package com.somemore.recruitboard.domain;

import static com.somemore.recruitboard.domain.RecruitStatus.RECRUITING;
import static com.somemore.recruitboard.domain.VolunteerType.OTHER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        RecruitBoard board = RecruitBoard.builder()
            .centerId(UUID.randomUUID())
            .locationId(1L)
            .title("봉사모집제목")
            .content("봉사모집내용")
            .region("경기")
            .recruitmentCount(10)
            .imgUrl("https://image.domain.com/links")
            .volunteerStartDateTime(LocalDateTime.now())
            .volunteerEndDateTime(LocalDateTime.now().plusHours(1))
            .volunteerType(OTHER)
            .admitted(true)
            .build();

        // when
        RecruitStatus recruitStatus = board.getRecruitStatus();

        // then
        assertThat(recruitStatus).isEqualTo(RECRUITING);
    }

    @DisplayName("봉사 종료 시간이 시작 시간과 같거나 빠르면, 봉사 모집글 생성 시 에러가 발생한다")
    @ParameterizedTest
    @ValueSource(longs = {0, -1})
    void createRecruitBoardWithInValidVolunteerTime(long secondsOffset) {
        // given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endDateTime = now.plusSeconds(secondsOffset);

        // when & then
        assertThatThrownBy(
            () -> RecruitBoard.builder()
                .centerId(UUID.randomUUID())
                .locationId(1L)
                .title("봉사모집제목")
                .content("봉사모집내용")
                .region("경기")
                .recruitmentCount(10)
                .imgUrl("https://image.domain.com/links")
                .volunteerStartDateTime(now)
                .volunteerEndDateTime(endDateTime)
                .volunteerType(VolunteerType.OTHER)
                .admitted(true)
                .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("봉사 시간을 계산할 수 있다")
    @Test
    void testCalculateVolunteerTime() {
        // given
        int hours = 3;
        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime endDateTime = startDateTime.plusHours(hours);

        RecruitBoard board = RecruitBoard.builder()
            .centerId(UUID.randomUUID())
            .locationId(1L)
            .title("봉사모집제목")
            .content("봉사모집내용")
            .region("경기")
            .recruitmentCount(10)
            .imgUrl("https://image.domain.com/links")
            .volunteerStartDateTime(startDateTime)
            .volunteerEndDateTime(endDateTime)
            .volunteerType(OTHER)
            .admitted(true)
            .build();

        // when
        LocalTime volunteerTime = board.calculateVolunteerTime();

        // then
        assertThat(volunteerTime).isEqualTo(LocalTime.of(hours, 0));
    }
}