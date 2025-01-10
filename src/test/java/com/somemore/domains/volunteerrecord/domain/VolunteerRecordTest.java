package com.somemore.domains.volunteerrecord.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class VolunteerRecordTest {

    @Test
    void createVolunteerRecord_success() {
        // Given
        UUID volunteerId = UUID.randomUUID();
        String title = "서울 도서관 봉사";
        LocalDate volunteerDate = LocalDate.of(2025, 1, 8);
        int volunteerHours = 4;

        // When
        VolunteerRecord volunteerRecord = VolunteerRecord.create(volunteerId, title, volunteerDate, volunteerHours);

        // Then
        assertThat(volunteerRecord).isNotNull();
        assertThat(volunteerRecord.getVolunteerId()).isEqualTo(volunteerId);
        assertThat(volunteerRecord.getTitle()).isEqualTo(title);
        assertThat(volunteerRecord.getVolunteerDate()).isEqualTo(volunteerDate);
        assertThat(volunteerRecord.getVolunteerHours()).isEqualTo(volunteerHours);
    }
}
