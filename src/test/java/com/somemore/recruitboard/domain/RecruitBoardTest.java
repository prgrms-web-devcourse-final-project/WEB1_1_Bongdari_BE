package com.somemore.recruitboard.domain;

import static com.somemore.recruitboard.domain.VolunteerType.OTHER;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RecruitBoardTest {

    @DisplayName("봉사 모집글 생성시 모집상태는 모집중이다")
    @Test
    void CreateRecruitBoardWithDefaultStatus() {
        // given
        RecruitBoard board = RecruitBoard.builder()
            .locationId(1L)
            .centerId(1L)
            .title("1234")
            .content("123")
            .imgUrl("123")
            .volunteerDate(LocalDateTime.now())
            .volunteerType(OTHER)
            .volunteerHours(7)
            .admitted(true)
            .build();

        // when
        RecruitStatus recruitStatus = board.getRecruitStatus();

        // then
        assertThat(recruitStatus).isEqualTo(RecruitStatus.RECRUITING);
    }

}