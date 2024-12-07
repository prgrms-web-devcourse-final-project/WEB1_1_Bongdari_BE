package com.somemore.volunteer.domain;

import static com.somemore.auth.oauth.OAuthProvider.NAVER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class VolunteerTest {

    @DisplayName("봉사 스텟을 업데이트할 수 있다")
    @Test
    void updateVolunteerStats() {
        // given
        int hour = 8;
        int cnt = 1;
        Volunteer volunteer = Volunteer.createDefault(NAVER, "naver");

        // when
        volunteer.updateVolunteerStats(hour, cnt);

        // then
        assertThat(volunteer.getTotalVolunteerCount()).isEqualTo(cnt);
        assertThat(volunteer.getTotalVolunteerHours()).isEqualTo(hour);
    }
}
