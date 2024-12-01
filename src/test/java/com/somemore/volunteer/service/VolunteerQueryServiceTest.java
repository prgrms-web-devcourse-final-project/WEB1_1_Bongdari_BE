package com.somemore.volunteer.service;

import com.somemore.IntegrationTestSupport;
import com.somemore.auth.oauth.OAuthProvider;
import com.somemore.global.exception.BadRequestException;
import com.somemore.volunteer.domain.Volunteer;
import com.somemore.volunteer.domain.VolunteerDetail;
import com.somemore.volunteer.dto.request.VolunteerRegisterRequestDto;
import com.somemore.volunteer.dto.response.VolunteerResponseDto;
import com.somemore.volunteer.repository.VolunteerDetailRepository;
import com.somemore.volunteer.repository.VolunteerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_VOLUNTEER;
import static com.somemore.global.exception.ExceptionMessage.UNAUTHORIZED_VOLUNTEER_DETAIL;
import static com.somemore.volunteer.domain.Volunteer.createDefault;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class VolunteerQueryServiceTest extends IntegrationTestSupport {

    @Autowired
    private VolunteerQueryService volunteerQueryService;

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private VolunteerDetailRepository volunteerDetailRepository;

    final String oAuthId = "example-oauth-id";
    final OAuthProvider oAuthProvider = OAuthProvider.NAVER;


    @DisplayName("존재하는 OAuth ID로 봉사자 ID를 조회한다")
    @Test
    void getVolunteerIdByOAuthId() {
        // given
        Volunteer volunteer = createDefault(oAuthProvider, oAuthId);
        volunteerRepository.save(volunteer);

        // when
        UUID actualId = volunteerQueryService.getVolunteerIdByOAuthId(oAuthId);

        // then
        assertThat(actualId)
                .isNotNull()
                .isEqualTo(volunteer.getId());
    }

    @DisplayName("존재하지 않는 OAuth ID로 조회 시 예외를 던진다")
    @Test
    void getVolunteerIdByNonExistOAuthId() {
        // when
        // then
        assertThatThrownBy(() -> volunteerQueryService.getVolunteerIdByOAuthId(oAuthId))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(NOT_EXISTS_VOLUNTEER.getMessage());
    }

    @DisplayName("봉사자의 id로 nickname을 조회한다.")
    @Test
    void getNicknameById() {
        //given
        Volunteer volunteer = createDefault(oAuthProvider, oAuthId);
        volunteerRepository.save(volunteer);

        //when
        String nickname = volunteerQueryService.getNicknameById(volunteer.getId());

        //then
        assertThat(nickname).isEqualTo(volunteer.getNickname());
    }

    @DisplayName("존재하지 않는 봉사자 ID로 닉네임 조회 시 예외를 던진다")
    @Test
    void throwExceptionWhenNicknameNotFound() {
        // given
        UUID volunteerId = UUID.randomUUID();

        // when
        // then
        assertThatThrownBy(() -> volunteerQueryService.getNicknameById(volunteerId))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(NOT_EXISTS_VOLUNTEER.getMessage());
    }

    @DisplayName("내 프로필 조회 성공")
    @Test
    void getMyProfile() {
        // given
        Volunteer volunteer = createDefault(oAuthProvider, oAuthId);
        volunteerRepository.save(volunteer);
        UUID volunteerId = volunteer.getId();

        VolunteerDetail volunteerDetail = createVolunteerDetail(volunteerId);
        volunteerDetailRepository.save(volunteerDetail);

        // when
        VolunteerResponseDto response = volunteerQueryService.getMyProfile(volunteerId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.volunteerId()).isEqualTo(volunteerId.toString());
        assertThat(response.nickname()).isEqualTo(volunteer.getNickname());
    }

    @DisplayName("봉사자 프로필 조회 성공")
    @Test
    void getVolunteerProfile() {
        // given
        Volunteer volunteer = createDefault(oAuthProvider, oAuthId);
        volunteerRepository.save(volunteer);
        UUID volunteerId = volunteer.getId();

        VolunteerDetail volunteerDetail = createVolunteerDetail(volunteerId);
        volunteerDetailRepository.save(volunteerDetail);

        // when
        VolunteerResponseDto response = volunteerQueryService.getVolunteerProfile(volunteerId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.volunteerId()).isEqualTo(volunteerId.toString());
        assertThat(response.nickname()).isEqualTo(volunteer.getNickname());
        assertThat(response.volunteerDetailResponseDto()).isNull();
    }

    @DisplayName("권한이 없는 기관의 봉사자 상세 프로필 조회 실패")
    @Test
    void getVolunteerDetailedProfile() {
        // given
        UUID centerId = UUID.randomUUID();

        Volunteer volunteer = createDefault(oAuthProvider, oAuthId);
        volunteerRepository.save(volunteer);
        UUID volunteerId = volunteer.getId();

        VolunteerDetail volunteerDetail = createVolunteerDetail(volunteerId);
        volunteerDetailRepository.save(volunteerDetail);

        // when
        // then
        assertThatThrownBy(() -> volunteerQueryService.getVolunteerDetailedProfile(volunteerId, centerId))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(UNAUTHORIZED_VOLUNTEER_DETAIL.getMessage());
    }

    private static VolunteerDetail createVolunteerDetail(UUID volunteerId) {

        VolunteerRegisterRequestDto volunteerRegisterRequestDto =
                new VolunteerRegisterRequestDto(
                        OAuthProvider.NAVER,
                        "example-oauth-id",
                        "making",
                        "making@example.com",
                        "male",
                        "06-08",
                        "1998",
                        "010-1234-5678"
                );

        return VolunteerDetail.of(volunteerRegisterRequestDto, volunteerId);
    }
}
