package com.somemore.volunteer.service;

import com.somemore.support.IntegrationTestSupport;
import com.somemore.volunteer.domain.NEWVolunteer;
import com.somemore.volunteer.repository.NEWVolunteerRepository;
import com.somemore.volunteer.repository.record.VolunteerNicknameAndId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    @DisplayName("봉사자 ID로 봉사자를 조회한다")
    void getById() {
        NEWVolunteer foundVolunteer = volunteerQueryService.getById(volunteer.getId());

        assertThat(foundVolunteer).isEqualTo(volunteer);
    }

    @Test
    @DisplayName("유저 ID로 봉사자를 조회한다")
    void getByUserId() {
        NEWVolunteer foundVolunteer = volunteerQueryService.getByUserId(userId);

        assertThat(foundVolunteer).isEqualTo(volunteer);
    }

    @Test
    @DisplayName("유저 ID로 봉사자 ID를 조회한다")
    void getIdByUserId() {
        UUID foundVolunteerId = volunteerQueryService.getIdByUserId(userId);

        assertThat(foundVolunteerId).isEqualTo(volunteer.getId());
    }

    @Test
    @DisplayName("봉사자 ID로 유저 ID를 조회한다")
    void getUserIdById() {
        UUID foundUserId = volunteerQueryService.getUserIdById(volunteer.getId());

        assertThat(foundUserId).isEqualTo(volunteer.getUserId());
    }

    @DisplayName("아이디 리스트로 VolunteerNicknameAndId 리스트를 조회할 수 있다.")
    @Test
    void findVolunteerNicknameAndIdsByIds() {
        // given
        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();
        UUID userId3 = UUID.randomUUID();

        NEWVolunteer volunteer1 = NEWVolunteer.createDefault(userId1);
        NEWVolunteer volunteer2 = NEWVolunteer.createDefault(userId2);
        NEWVolunteer volunteer3 = NEWVolunteer.createDefault(userId3);

        volunteerRepository.save(volunteer1);
        volunteerRepository.save(volunteer2);
        volunteerRepository.save(volunteer3);

        List<UUID> ids = List.of(volunteer1.getId(), volunteer2.getId(), volunteer3.getId());

        // when
        List<VolunteerNicknameAndId> nicknames = volunteerQueryService.getVolunteerNicknameAndIdsByIds(ids);

        // then
        assertThat(nicknames).extracting(VolunteerNicknameAndId::userId)
                .containsExactlyInAnyOrder(
                        volunteer1.getUserId(),
                        volunteer2.getUserId(),
                        volunteer3.getUserId());
    }
}
