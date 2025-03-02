package com.somemore.volunteer.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.somemore.support.IntegrationTestSupport;
import com.somemore.volunteer.domain.NEWVolunteer;
import com.somemore.volunteer.repository.record.VolunteerNickname;
import com.somemore.volunteer.repository.record.VolunteerNicknameAndId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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

    @DisplayName("아이디로 봉사자 닉네임을 조회할 수 있다.")
    @Test
    void findNicknameById() {
        // given
        NEWVolunteer volunteer = NEWVolunteer.createDefault(UUID.randomUUID());
        volunteerRepository.save(volunteer);

        // when
        Optional<String> nickname = volunteerRepository.findNicknameById(volunteer.getId());

        // then
        assertThat(nickname)
                .isPresent()
                .contains(volunteer.getNickname());
    }

    @DisplayName("id 리스트로 nickname 리스트를 조회할 수 있다.")
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
        List<VolunteerNickname> nicknames = volunteerRepository.findNicknamesByIds(ids);

        // then
        assertThat(nicknames).extracting(VolunteerNickname::nickname)
                .containsExactlyInAnyOrder(
                        volunteer1.getNickname(),
                        volunteer2.getNickname(),
                        volunteer3.getNickname());
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
        List<VolunteerNicknameAndId> nicknames = volunteerRepository.findVolunteerNicknameAndIdsByIds(
                ids);

        // then
        assertThat(nicknames).extracting(VolunteerNicknameAndId::userId)
                .containsExactlyInAnyOrder(
                        volunteer1.getUserId(),
                        volunteer2.getUserId(),
                        volunteer3.getUserId());
    }

    @DisplayName("아이디로 존재 여부를 조회할 수 있다.")
    @Test
    void existsById() {
        // given
        NEWVolunteer volunteer = NEWVolunteer.createDefault(UUID.randomUUID());
        volunteerRepository.save(volunteer);
        UUID id = volunteer.getId();

        // when
        boolean result = volunteerRepository.existsById(id);

        // then
        assertThat(result).isTrue();
    }
}
