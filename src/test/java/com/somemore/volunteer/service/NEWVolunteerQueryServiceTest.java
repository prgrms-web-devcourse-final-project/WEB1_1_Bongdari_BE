package com.somemore.volunteer.service;

import com.somemore.support.IntegrationTestSupport;
import com.somemore.volunteer.domain.NEWVolunteer;
import com.somemore.volunteer.repository.NEWVolunteerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class NEWVolunteerQueryServiceTest extends IntegrationTestSupport {

    @Autowired
    private NEWVolunteerQueryService volunteerQueryService;

    @Autowired
    private NEWVolunteerRepository volunteerRepository;

    UUID userId;
    NEWVolunteer volunteer;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        volunteer = NEWVolunteer.createDefault(userId);
        volunteerRepository.save(volunteer);
    }

    @Test
    @DisplayName("사용자 ID로 봉사자를 조회한다")
    void getByUserId() {
        NEWVolunteer foundVolunteer = volunteerQueryService.getByUserId(userId);

        assertThat(foundVolunteer).isEqualTo(volunteer);
    }

    @Test
    @DisplayName("사용자 ID로 봉사자 ID를 조회한다")
    void getIdByUserId() {
        UUID foundVolunteerId = volunteerQueryService.getIdByUserId(userId);

        assertThat(foundVolunteerId).isEqualTo(volunteer.getId());
    }
}