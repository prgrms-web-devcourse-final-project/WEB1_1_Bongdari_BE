package com.somemore.volunteer.service;

import com.somemore.support.IntegrationTestSupport;
import com.somemore.volunteer.domain.NEWVolunteer;
import com.somemore.volunteer.repository.NEWVolunteerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class GetNicknamesByIdsServiceTest extends IntegrationTestSupport {

    @Autowired
    private GetVolunteerNicknamesByIdsService getNicknamesByIdsService;

    @Autowired
    private NEWVolunteerRepository volunteerRepository;

    @DisplayName("봉사자 id 리스트로 nickname 리스트를 조회할 수 있다. (service)")
    @Test
    void findNicknamesByIds() {

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
        List<String> nicknames = getNicknamesByIdsService.getNicknamesByIds(ids);

        // then
        assertThat(nicknames).containsExactlyInAnyOrder(
                volunteer1.getNickname(),
                volunteer2.getNickname(),
                volunteer3.getNickname());
    }

}