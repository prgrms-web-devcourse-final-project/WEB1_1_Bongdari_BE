package com.somemore.volunteer.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NEWVolunteerTest {

    @DisplayName("봉사 스텟을 업데이트할 수 있다")
    @Test
    void updateVolunteerStats() {
        // given
        int hour = 8;
        int cnt = 1;
        NEWVolunteer volunteer = createVolunteer();

        // when
        volunteer.updateVolunteerStats(hour, cnt);

        // then
        assertThat(volunteer.getTotalVolunteerCount()).isEqualTo(cnt);
        assertThat(volunteer.getTotalVolunteerHours()).isEqualTo(hour);
    }

    private NEWVolunteer createVolunteer() {
        UUID userId = UUID.randomUUID();

        return NEWVolunteer.createDefault(userId);
    }

}
