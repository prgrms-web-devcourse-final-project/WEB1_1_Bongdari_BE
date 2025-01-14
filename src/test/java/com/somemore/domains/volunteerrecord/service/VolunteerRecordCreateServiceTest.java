package com.somemore.domains.volunteerrecord.service;

import com.somemore.domains.volunteerrecord.domain.VolunteerRecord;
import com.somemore.domains.volunteerrecord.repository.VolunteerRecordJpaRepository;
import com.somemore.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class VolunteerRecordCreateServiceTest extends IntegrationTestSupport {

    @Autowired
    private VolunteerRecordCreateService volunteerRecordCreateService;

    @Autowired
    private VolunteerRecordJpaRepository volunteerRecordJpaRepository;

    @DisplayName("봉사 기록을 저장할 수 있다.")
    @Test
    void createVolunteerRecord() {
        //given
        UUID volunteerId = UUID.randomUUID();
        VolunteerRecord volunteerRecord = VolunteerRecord.create(
                volunteerId,
                "서울 도서관 봉사",
                LocalDate.now(),
                4
        );

        //when
        volunteerRecordCreateService.create(volunteerRecord);

        //then
        VolunteerRecord savedRecord = volunteerRecordJpaRepository.findById(volunteerRecord.getId())
                .orElseThrow(() -> new AssertionError("저장되지 않은 데이터"));

        assertThat(savedRecord).isNotNull();
        assertThat(savedRecord.getVolunteerId()).isEqualTo(volunteerId);
        assertThat(savedRecord.getTitle()).isEqualTo("서울 도서관 봉사");
        assertThat(savedRecord.getVolunteerDate()).isEqualTo(volunteerRecord.getVolunteerDate());
        assertThat(savedRecord.getVolunteerHours()).isEqualTo(4);
    }

}
