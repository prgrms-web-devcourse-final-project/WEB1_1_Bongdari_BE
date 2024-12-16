package com.somemore.volunteer.service;

import com.somemore.support.IntegrationTestSupport;
import com.somemore.global.auth.oauth.OAuthProvider;
import com.somemore.volunteer.domain.Volunteer;
import com.somemore.volunteer.dto.request.VolunteerProfileUpdateRequestDto;
import com.somemore.volunteer.repository.VolunteerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_VOLUNTEER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class UpdateVolunteerProfileServiceTest extends IntegrationTestSupport {

    @Autowired
    private UpdateVolunteerProfileService updateVolunteerProfileService;

    @Autowired
    private VolunteerRepository volunteerRepository;

    final String oAuthId = "example-oauth-id";
    final OAuthProvider oAuthProvider = OAuthProvider.NAVER;
    final String imgUrl = "http://example.com/updated-image.jpg";
    final VolunteerProfileUpdateRequestDto requestDto = new VolunteerProfileUpdateRequestDto(
            "Updated Nickname",
            "Updated Introduction"
    );


    @Test
    @DisplayName("봉사자 프로필을 성공적으로 업데이트한다")
    void updateVolunteerProfileSuccess() {
        // given
        Volunteer volunteer = Volunteer.createDefault(oAuthProvider, oAuthId);
        volunteerRepository.save(volunteer);

        // when
        updateVolunteerProfileService.update(volunteer.getId(), requestDto, imgUrl);

        // then
        Volunteer updatedVolunteer = volunteerRepository.findById(volunteer.getId()).orElseThrow();
        assertThat(updatedVolunteer.getNickname()).isEqualTo(requestDto.nickname());
        assertThat(updatedVolunteer.getIntroduce()).isEqualTo(requestDto.introduce());
        assertThat(updatedVolunteer.getImgUrl()).isEqualTo(imgUrl);
    }

    @Test
    @DisplayName("존재하지 않는 봉사자 ID로 업데이트 시 예외를 던진다")
    void updateVolunteerProfileThrowsWhenNotFound() {
        // given
        // when
        // then
        assertThatThrownBy(() -> updateVolunteerProfileService.update(UUID.randomUUID(), requestDto, imgUrl))
                .isInstanceOf(com.somemore.global.exception.BadRequestException.class)
                .hasMessage(NOT_EXISTS_VOLUNTEER.getMessage());
    }
}
