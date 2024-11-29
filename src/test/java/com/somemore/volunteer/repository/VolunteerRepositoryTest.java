package com.somemore.volunteer.repository;

import com.somemore.IntegrationTestSupport;
import com.somemore.auth.oauth.OAuthProvider;
import com.somemore.volunteer.domain.Volunteer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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
}