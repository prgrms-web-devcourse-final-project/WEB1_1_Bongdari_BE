package com.somemore.volunteer.repository;

import com.somemore.support.IntegrationTestSupport;
import com.somemore.volunteer.domain.NEWVolunteer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class NEWVolunteerRepositoryImplTest extends IntegrationTestSupport {

    @Autowired
    private NEWVolunteerRepositoryImpl volunteerRepository;

    @DisplayName("유저 아이디로 봉사자를 등록할 수 있다.")
    @Test
    void saveVolunteerByUserId() {
        // given
        UUID userId = UUID.randomUUID();
        NEWVolunteer volunteer = NEWVolunteer.createDefault(userId);

        // when
        volunteerRepository.save(volunteer);

        // then
        NEWVolunteer volunteerByUserId = volunteerRepository.findByUserId(userId).orElseThrow();
        NEWVolunteer volunteerById = volunteerRepository.findById(volunteer.getId()).orElseThrow();


        assertThat(volunteer)
                .isEqualTo(volunteerByUserId)
                .isEqualTo(volunteerById);

    }
}
