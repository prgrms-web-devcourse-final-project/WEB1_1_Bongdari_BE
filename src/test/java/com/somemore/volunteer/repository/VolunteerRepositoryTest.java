package com.somemore.volunteer.repository;

import com.somemore.IntegrationTestSupport;
import com.somemore.auth.oauth.OAuthProvider;
import com.somemore.volunteer.domain.Volunteer;
import com.somemore.volunteer.repository.mapper.VolunteerOverviewForRankingByHours;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class VolunteerRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private VolunteerRepository volunteerRepository;

    String oAuthId;
    Volunteer volunteer;

    @BeforeEach
    void setup() {
        oAuthId = "example-oauth-id";
        volunteer = Volunteer.createDefault(OAuthProvider.NAVER, oAuthId);
        volunteerRepository.save(volunteer);
    }

    @DisplayName("봉사자의 id로 닉네임을 조회한다.")
    @Test
    void findNicknameById() {
        // when
        String volunteerNickname = volunteerRepository.findNicknameById(volunteer.getId());

        // then
        assertThat(volunteerNickname).isEqualTo(volunteer.getNickname());
    }

    @DisplayName("올바르지 않은 봉사자의 id로 닉네임을 조회하면 null을 반환한다.")
    @Test
    void findNicknameByInvalidId() {
        // given
        UUID inValidVolunteerId = UUID.randomUUID();

        // when
        String volunteerNickname = volunteerRepository.findNicknameById(inValidVolunteerId);

        // then
        assertThat(volunteerNickname).isNull();
    }

    @DisplayName("봉사자의 id로 봉사자 정보를 조회한다.")
    @Test
    void findById() {
        // when
        Optional<Volunteer> foundVolunteer = volunteerRepository.findById(volunteer.getId());

        // then
        assertThat(foundVolunteer).isPresent();
        assertThat(foundVolunteer.get().getId()).isEqualTo(volunteer.getId());
        assertThat(foundVolunteer.get().getNickname()).isEqualTo(volunteer.getNickname());
    }

    @DisplayName("OAuth ID로 봉사자 정보를 조회한다.")
    @Test
    void findByOauthId() {
        // when
        Optional<Volunteer> foundVolunteer = volunteerRepository.findByOauthId(oAuthId);

        // then
        assertThat(foundVolunteer).isPresent();
        assertThat(foundVolunteer.get().getOauthId()).isEqualTo(oAuthId);
        assertThat(foundVolunteer.get().getNickname()).isEqualTo(volunteer.getNickname());
    }

    @DisplayName("봉사 시간 기준 상위 4명을 조회한다.")
    @Test
    void findRankingByVolunteerHours_top4() {
        // given
        for (int i = 1; i <= 5; i++) {
            createVolunteerAndUpdateVolunteerStats(i);
        }

        // when
        List<VolunteerOverviewForRankingByHours> rankings = volunteerRepository.findRankingByVolunteerHours();

        // then
        assertThat(rankings).hasSize(4);
        assertThat(rankings.get(0).totalVolunteerHours()).isGreaterThan(rankings.get(1).totalVolunteerHours());
    }

    @DisplayName("등록된 봉사자가 없는 경우 빈 리스트를 반환한다.")
    @Test
    void findRankingByVolunteerHours_noVolunteers() {
        // given
        volunteerRepository.deleteAllInBatch();

        // when
        List<VolunteerOverviewForRankingByHours> rankings = volunteerRepository.findRankingByVolunteerHours();

        // then
        assertThat(rankings).isEmpty();
    }

    @DisplayName("등록된 봉사자가 4명 이하인 경우 전체 봉사자를 반환한다.")
    @Test
    void findRankingByVolunteerHours_lessThan4Volunteers() {
        // given
        volunteerRepository.deleteAllInBatch();

        for (int i = 1; i <= 3; i++) {
            createVolunteerAndUpdateVolunteerStats(i);
        }

        // when
        List<VolunteerOverviewForRankingByHours> rankings = volunteerRepository.findRankingByVolunteerHours();

        // then
        assertThat(rankings).hasSize(3);
        assertThat(rankings.get(0).totalVolunteerHours()).isGreaterThan(rankings.get(1).totalVolunteerHours());
    }

    private void createVolunteerAndUpdateVolunteerStats(int i) {
        Volunteer newVolunteer = Volunteer.createDefault(OAuthProvider.NAVER, "oauth-id-" + i);
        newVolunteer.updateVolunteerStats(i * 10, i);
        volunteerRepository.save(newVolunteer);
    }

    @DisplayName("아이디 리스트로 봉사자를 조회할 수있다.")
    @Test
    void findAllByIds() {
        // given
        Volunteer volunteer1 = Volunteer.createDefault(OAuthProvider.NAVER, "1234");
        Volunteer volunteer2 = Volunteer.createDefault(OAuthProvider.NAVER, "1234");
        Volunteer volunteer3 = Volunteer.createDefault(OAuthProvider.NAVER, "1234");
        Volunteer volunteer4 = Volunteer.createDefault(OAuthProvider.NAVER, "1234");
        volunteer4.markAsDeleted();

        volunteerRepository.save(volunteer1);
        volunteerRepository.save(volunteer2);
        volunteerRepository.save(volunteer3);
        volunteerRepository.save(volunteer4);

        // when
        List<Volunteer> volunteers = volunteerRepository.findAllByIds(
                List.of(volunteer1.getId(), volunteer2.getId(), volunteer3.getId(),
                        volunteer4.getId()
                ));

        // then
        assertThat(volunteers).hasSize(3);
    }
}
