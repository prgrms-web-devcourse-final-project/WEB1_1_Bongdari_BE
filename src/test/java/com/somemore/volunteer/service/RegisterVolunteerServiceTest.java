package com.somemore.volunteer.service;

import com.somemore.IntegrationTestSupport;
import com.somemore.global.auth.oauth.OAuthProvider;
import com.somemore.volunteer.domain.Gender;
import com.somemore.volunteer.domain.Tier;
import com.somemore.volunteer.domain.Volunteer;
import com.somemore.volunteer.domain.VolunteerDetail;
import com.somemore.volunteer.dto.request.VolunteerRegisterRequestDto;
import com.somemore.volunteer.repository.VolunteerDetailRepository;
import com.somemore.volunteer.repository.VolunteerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class RegisterVolunteerServiceTest extends IntegrationTestSupport {

    @Autowired
    private RegisterVolunteerService registerVolunteerService;

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private VolunteerDetailRepository volunteerDetailRepository;

    @DisplayName("봉사자와 상세 정보를 저장한다")
    @Test
    void registerVolunteer() {
        // given
        VolunteerRegisterRequestDto dto = new VolunteerRegisterRequestDto(
                OAuthProvider.NAVER,
                "oauth-id-example",
                "making",
                "making@example.com",
                "M",
                "06-08",
                "1998",
                "010-1234-5678"
        );

        // when
        registerVolunteerService.registerVolunteer(dto);

        // then
        Volunteer volunteer = volunteerRepository.findByOauthId("oauth-id-example")
                .orElseThrow(EntityNotFoundException::new);
        VolunteerDetail volunteerDetail = volunteerDetailRepository.findByVolunteerId(volunteer.getId())
                .orElseThrow(EntityNotFoundException::new);

        // Volunteer
        assertThat(volunteer.getOauthProvider()).isEqualTo(OAuthProvider.NAVER);
        assertThat(volunteer.getOauthId()).isEqualTo("oauth-id-example");
        assertThat(volunteer.getNickname()).hasSize(8); // 8자리 default UUID
        assertThat(volunteer.getImgUrl()).isEqualTo(""); // default
        assertThat(volunteer.getIntroduce()).isEqualTo(""); // default
        assertThat(volunteer.getTier()).isEqualTo(Tier.RED); // default
        assertThat(volunteer.getTotalVolunteerHours()).isEqualTo(0); // default
        assertThat(volunteer.getTotalVolunteerCount()).isEqualTo(0); // default

        // VolunteerDetail
        assertThat(volunteerDetail.getVolunteerId()).isEqualTo(volunteer.getId());
        assertThat(volunteerDetail.getName()).isEqualTo("making");
        assertThat(volunteerDetail.getEmail()).isEqualTo("making@example.com");
        assertThat(volunteerDetail.getGender()).isEqualTo(Gender.MALE);
        assertThat(volunteerDetail.getBirthDate()).isEqualTo("1998-06-08");
        assertThat(volunteerDetail.getContactNumber()).isEqualTo("010-1234-5678");
    }
}
