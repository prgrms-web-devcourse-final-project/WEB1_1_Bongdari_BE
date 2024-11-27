package com.somemore.volunteer.service;

import com.somemore.IntegrationTestSupport;
import com.somemore.auth.oauth.OAuthProvider;
import com.somemore.volunteer.domain.Volunteer;
import com.somemore.volunteer.repository.VolunteerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FindVolunteerIdServiceTest extends IntegrationTestSupport {

    @Autowired
    private FindVolunteerIdService findVolunteerIdService;

    @Autowired
    private VolunteerRepository volunteerRepository;

    @AfterEach
    void tearDown() {
        volunteerRepository.deleteAllInBatch();
    }

    @DisplayName("존재하는 OAuth ID로 봉사자 ID를 조회한다")
    @Test
    void findVolunteerId() {
        // given
        String oAuthId = "example-oauth-id";
        Volunteer volunteer = Volunteer.createDefault(OAuthProvider.NAVER, oAuthId);

        volunteerRepository.save(volunteer);

        // when
        UUID actualId = findVolunteerIdService.findVolunteerIdByOAuthId(oAuthId);

        // then
        assertThat(actualId)
                .isNotNull()
                .isEqualTo(volunteer.getId());
    }

    @DisplayName("존재하지 않는 OAuth ID로 조회 시 예외를 던진다")
    @Test
    void throwExceptionWhenVolunteerNotFound() {
        // given
        String oAuthId = "non-existing-oauth-id";

        // when
        // then
        assertThatThrownBy(() -> findVolunteerIdService.findVolunteerIdByOAuthId(oAuthId))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("봉사자의 id로 nickname을 조회한다.")
    @Test
    void getNicknameById() {

        //given
        String oAuthId = "example-oauth-id";
        Volunteer volunteer = Volunteer.createDefault(OAuthProvider.NAVER, oAuthId);

        volunteerRepository.save(volunteer);

        //when
        String nickname = findVolunteerIdService.getNicknameById(volunteer.getId());

        //then
        assertThat(nickname).isEqualTo(volunteer.getNickname());
    }
}
