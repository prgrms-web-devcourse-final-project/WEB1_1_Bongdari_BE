package com.somemore.recruitboard.domain;

import static com.somemore.recruitboard.domain.RecruitStatus.RECRUITING;
import static com.somemore.recruitboard.domain.VolunteerType.OTHER;
import static org.assertj.core.api.Assertions.assertThat;

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
        Long locationId = 1L;
        RecruitBoard board = createRecruitBoard(centerId, locationId);

        // when
        RecruitStatus recruitStatus = board.getRecruitStatus();

        // then
        assertThat(board.getCenterId()).isEqualTo(centerId);
        assertThat(board.getLocationId()).isEqualTo(locationId);
        assertThat(recruitStatus).isEqualTo(RECRUITING);
    }

    @DisplayName("봉사 시간을 계산할 수 있다")
    @Test
    void testCalculateVolunteerTime() {
        // given
        int hours = 3;
        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime endDateTime = startDateTime.plusHours(hours);

        RecruitBoard board = createRecruitBoard(startDateTime, endDateTime);

        // when
        LocalTime volunteerTime = board.getVolunteerHours();

        // then
        assertThat(volunteerTime).isEqualTo(LocalTime.of(hours, 0));
    }

    private static RecruitBoard createRecruitBoard(UUID centerId, Long locationId) {
        VolunteerInfo volunteerInfo = VolunteerInfo.builder()
            .region("경기")
            .recruitmentCount(1)
            .volunteerStartDateTime(LocalDateTime.now())
            .volunteerEndDateTime(LocalDateTime.now().plusHours(1))
            .volunteerType(OTHER)
            .admitted(true)
            .build();

        return RecruitBoard.builder()
            .centerId(centerId)
            .locationId(locationId)
            .title("봉사모집제목")
            .content("봉사모집내용")
            .imgUrl("https://image.domain.com/links")
            .volunteerInfo(volunteerInfo)
            .build();
    }

    private static RecruitBoard createRecruitBoard(LocalDateTime startDateTime,
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
            .centerId(UUID.randomUUID())
            .locationId(1L)
            .title("봉사모집제목")
            .content("봉사모집내용")
            .imgUrl("https://image.domain.com/links")
            .volunteerInfo(volunteerInfo)
            .build();
    }

}