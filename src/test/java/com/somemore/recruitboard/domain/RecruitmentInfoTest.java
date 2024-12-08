package com.somemore.recruitboard.domain;

import static com.somemore.common.fixture.LocalDateTimeFixture.createStartDateTime;
import static com.somemore.common.fixture.LocalDateTimeFixture.createUpdateStartDateTime;
import static com.somemore.recruitboard.domain.VolunteerCategory.ADMINISTRATIVE_SUPPORT;
import static com.somemore.recruitboard.domain.VolunteerCategory.OTHER;
import static com.somemore.recruitboard.domain.VolunteerCategory.SAFETY_PREVENTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class RecruitmentInfoTest {

    @DisplayName("봉사 종료 시간이 시작 시간과 같거나 빠르면, 봉사 모집글 생성 시 에러가 발생한다")
    @ParameterizedTest
    @ValueSource(longs = {0, -1})
    void createRecruitBoardWithInValidVolunteerTime(long minutesOffset) {
        // given
        LocalDateTime now = createStartDateTime();
        LocalDateTime endDateTime = now.plusMinutes(minutesOffset);

        // when & then
        assertThatThrownBy(
            () -> createRecruitmentInfo(now, endDateTime)
        ).isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("봉사 시간을 계산할 수 있다")
    @Test
    void testCalculateVolunteerTime() {
        // given
        int hours = 3;
        LocalDateTime startDateTime = createStartDateTime();
        LocalDateTime endDateTime = startDateTime.plusHours(hours);

        RecruitmentInfo recruitmentInfo = createRecruitmentInfo(startDateTime, endDateTime);

        // when
        LocalTime volunteerTime = recruitmentInfo.calculateVolunteerTime();

        // then
        assertThat(volunteerTime).isEqualTo(LocalTime.of(hours, 0));
    }

    @DisplayName("봉사 활동 정보를 업데이트 할 수 있다")
    @Test
    void updateRecruitmentInfo() {
        // given
        RecruitmentInfo recruitmentInfo = createRecruitmentInfo();

        String region = "서울특별시";
        Integer count = 2;
        VolunteerCategory volunteerCategory = SAFETY_PREVENTION;
        LocalDateTime startDateTime = createUpdateStartDateTime();
        LocalDateTime endDateTime = startDateTime.plusHours(2);
        Boolean admitted = false;

        // when
        recruitmentInfo.updateWith(region, count, volunteerCategory, startDateTime,
            endDateTime, admitted);

        // then
        assertThat(recruitmentInfo.getRecruitmentCount()).isEqualTo(count);
        assertThat(recruitmentInfo.getVolunteerCategory()).isEqualTo(volunteerCategory);
        assertThat(recruitmentInfo.getVolunteerStartDateTime().compareTo(startDateTime)).isZero();
        assertThat(recruitmentInfo.getVolunteerEndDateTime().compareTo(endDateTime)).isZero();
        assertThat(recruitmentInfo.getAdmitted()).isEqualTo(admitted);
    }

    @DisplayName("봉사활동 지역 정보를 업데이트할 수 있다")
    @Test
    void updateRecruitmentInfoWithRegion() {
        // given
        RecruitmentInfo recruitmentInfo = createRecruitmentInfo();
        String updateRegion = "새로운지역";

        // when
        recruitmentInfo.updateWith(updateRegion);

        // then
        assertThat(recruitmentInfo.getRegion()).isEqualTo(updateRegion);
    }

    @DisplayName("봉사 종료 시간이 시작 시간과 같거나 빠르면, 봉사 모집글을 업데이트시 에러가 발생한다")
    @ParameterizedTest
    @ValueSource(longs = {0, -1})
    void updateRecruitBoardWithInValidVolunteerTime(long minutesOffset) {
        // given
        LocalDateTime startDateTime = createStartDateTime();
        LocalDateTime endDateTime = startDateTime.plusMinutes(minutesOffset);

        RecruitmentInfo recruitmentInfo = createRecruitmentInfo();

        // when & then
        assertThatThrownBy(
            () -> recruitmentInfo.updateWith("",3, ADMINISTRATIVE_SUPPORT, startDateTime, endDateTime,
                false)
        ).isInstanceOf(IllegalArgumentException.class);

    }

    private static RecruitmentInfo createRecruitmentInfo(LocalDateTime startDateTime,
        LocalDateTime endDateTime) {
        return RecruitmentInfo.builder()
            .region("경기")
            .recruitmentCount(1)
            .volunteerStartDateTime(startDateTime)
            .volunteerEndDateTime(endDateTime)
            .volunteerCategory(OTHER)
            .admitted(true)
            .build();
    }

    private static RecruitmentInfo createRecruitmentInfo() {
        LocalDateTime startDateTime = createStartDateTime();
        LocalDateTime endDateTime = startDateTime.plusHours(1);
        return createRecruitmentInfo(startDateTime, endDateTime);
    }

}
