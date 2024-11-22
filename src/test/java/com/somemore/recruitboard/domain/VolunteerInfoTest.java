package com.somemore.recruitboard.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class VolunteerInfoTest {

    @DisplayName("봉사 종료 시간이 시작 시간과 같거나 빠르면, 봉사 모집글 생성 시 에러가 발생한다")
    @ParameterizedTest
    @ValueSource(longs = {0, -1})
    void createRecruitBoardWithInValidVolunteerTime(long secondsOffset) {
        // given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endDateTime = now.plusSeconds(secondsOffset);

        // when & then
        assertThatThrownBy(
            () -> createVolunteerInfo(now, endDateTime)
        ).isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("봉사 시간을 계산할 수 있다")
    @Test
    void testCalculateVolunteerTime() {
        // given
        int hours = 3;
        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime endDateTime = startDateTime.plusHours(hours);

        VolunteerInfo volunteerInfo = createVolunteerInfo(startDateTime, endDateTime);
        // when
        LocalTime volunteerTime = volunteerInfo.calculateVolunteerTime();

        // then
        assertThat(volunteerTime).isEqualTo(LocalTime.of(hours, 0));
    }

    private static VolunteerInfo createVolunteerInfo(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return VolunteerInfo.builder()
            .region("경기")
            .recruitmentCount(1)
            .volunteerStartDateTime(startDateTime)
            .volunteerEndDateTime(endDateTime)
            .volunteerType(VolunteerType.OTHER)
            .admitted(true)
            .build();
    }
}
