package com.somemore.domains.recruitboard.domain;

import static com.somemore.domains.recruitboard.domain.VolunteerCategory.OTHER;
import static com.somemore.domains.recruitboard.domain.VolunteerCategory.SAFETY_PREVENTION;
import static com.somemore.support.fixture.LocalDateTimeFixture.createStartDateTime;
import static com.somemore.support.fixture.LocalDateTimeFixture.createUpdateStartDateTime;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RecruitmentInfoTest {

    private RecruitmentInfo recruitmentInfo;

    @BeforeEach
    void setUp() {
        recruitmentInfo = createRecruitmentInfo();
    }

    @DisplayName("봉사 활동 정보를 업데이트 할 수 있다")
    @Test
    void updateRecruitmentInfo() {
        // given
        String region = "서울특별시";
        Integer count = 2;
        VolunteerCategory volunteerCategory = SAFETY_PREVENTION;
        LocalDateTime startDateTime = createUpdateStartDateTime();
        LocalDateTime endDateTime = startDateTime.plusHours(2);
        Integer volunteerHours = 2;
        Boolean admitted = false;

        // when
        recruitmentInfo.updateWith(region, count, volunteerCategory, startDateTime, endDateTime,
                volunteerHours, admitted);

        // then
        assertThat(recruitmentInfo.getRecruitmentCount()).isEqualTo(count);
        assertThat(recruitmentInfo.getVolunteerCategory()).isEqualTo(volunteerCategory);
        assertThat(recruitmentInfo.getVolunteerStartDateTime().compareTo(startDateTime)).isZero();
        assertThat(recruitmentInfo.getVolunteerEndDateTime().compareTo(endDateTime)).isZero();
        assertThat(recruitmentInfo.getVolunteerHours()).isEqualTo(volunteerHours);
        assertThat(recruitmentInfo.getAdmitted()).isEqualTo(admitted);
    }

    @DisplayName("봉사활동 지역 정보를 업데이트할 수 있다")
    @Test
    void updateRecruitmentInfoWithRegion() {
        // given
        String updateRegion = "새로운지역";

        // when
        recruitmentInfo.updateWith(updateRegion);

        // then
        assertThat(recruitmentInfo.getRegion()).isEqualTo(updateRegion);
    }

    private static RecruitmentInfo createRecruitmentInfo() {
        LocalDateTime startDateTime = createStartDateTime();
        LocalDateTime endDateTime = startDateTime.plusHours(1);

        return RecruitmentInfo.builder()
                .region("경기")
                .recruitmentCount(1)
                .volunteerStartDateTime(startDateTime)
                .volunteerEndDateTime(endDateTime)
                .volunteerHours(1)
                .volunteerCategory(OTHER)
                .admitted(true)
                .build();
    }

}
