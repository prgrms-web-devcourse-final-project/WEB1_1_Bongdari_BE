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
    void CreateRecruitBoardWithDefaultStatus() {
        // given
        RecruitBoard board = RecruitBoard.builder()
            .centerId(UUID.randomUUID())
            .locationId(1L)
            .title("봉사모집제목")
            .content("봉사모집내용")
            .region("경기")
            .recruitmentCount(10)
            .imgUrl("https://image.domain.com/links")
            .volunteerDate(LocalDateTime.now())
            .volunteerType(OTHER)
            .volunteerHours(LocalTime.of(7, 30))
            .admitted(true)
            .build();

        // when
        RecruitStatus recruitStatus = board.getRecruitStatus();

        // then
        assertThat(recruitStatus).isEqualTo(RECRUITING);
    }
}