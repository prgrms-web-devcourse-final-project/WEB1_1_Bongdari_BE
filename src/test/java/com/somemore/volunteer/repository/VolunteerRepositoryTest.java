package com.somemore.volunteer.repository;

import com.somemore.IntegrationTestSupport;
import com.somemore.auth.oauth.OAuthProvider;
import com.somemore.center.domain.Center;
import com.somemore.volunteer.domain.Volunteer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Transactional
public class VolunteerRepositoryTest extends IntegrationTestSupport {
    @Autowired
    private VolunteerRepository volunteerRepository;

    @DisplayName("봉사자의 id로 닉네임을 조회한다. (Repository)")
    @Test
    void findNicknameById() {
        //given
        String oAuthId = "example-oauth-id";
        Volunteer volunteer = Volunteer.createDefault(OAuthProvider.NAVER, oAuthId);

        volunteerRepository.save(volunteer);

        //when
        String volunteerNickname = volunteerRepository.findNicknameById(volunteer.getId());

        //then
        assertThat(volunteerNickname).isEqualTo(volunteer.getNickname());
    }
}
