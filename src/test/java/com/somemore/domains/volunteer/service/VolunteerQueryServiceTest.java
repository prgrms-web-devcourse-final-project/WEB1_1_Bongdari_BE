package com.somemore.domains.volunteer.service;

import com.somemore.domains.volunteer.domain.Volunteer;
import com.somemore.domains.volunteer.dto.response.VolunteerRankingResponseDto;
import com.somemore.domains.volunteer.repository.VolunteerRepository;
import com.somemore.global.auth.oauth.domain.OAuthProvider;
import com.somemore.global.exception.BadRequestException;
import com.somemore.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.somemore.domains.volunteer.domain.Volunteer.createDefault;
import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_VOLUNTEER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
class VolunteerQueryServiceTest extends IntegrationTestSupport {

    @Autowired
    private VolunteerQueryService volunteerQueryService;

    @Autowired
    private VolunteerRepository volunteerRepository;

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

    @DisplayName("봉사 시간 기준 상위 4명의 랭킹을 조회한다.")
    @Test
    void getRankingByHours() {
        // given
        for (int i = 1; i <= 5; i++) {
            Volunteer volunteer = Volunteer.createDefault(oAuthProvider, "oauth-id-" + i);
            volunteer.updateVolunteerStats(i * 10, i);
            volunteerRepository.save(volunteer);
        }

        // when
        VolunteerRankingResponseDto response = volunteerQueryService.getRankingByHours();

        // then
        assertThat(response).isNotNull();
        assertThat(response.rankings()).hasSize(4);

        List<Integer> hours = response.rankings().stream()
                .map(VolunteerRankingResponseDto.VolunteerOverview::totalVolunteerHours)
                .toList();
        assertThat(hours).isSortedAccordingTo((a, b) -> b - a);
    }

    @DisplayName("등록된 봉사자가 없는 경우 빈 랭킹 리스트를 반환한다.")
    @Test
    void getRankingByHours_noVolunteers() {
        // given
        volunteerRepository.deleteAllInBatch();

        // when
        VolunteerRankingResponseDto response = volunteerQueryService.getRankingByHours();

        // then
        assertThat(response).isNotNull();
        assertThat(response.rankings()).isEmpty();
    }

    @DisplayName("아이디 리스트로 봉사자를 조회할 수있다.")
    @Test
    void findAllByIds() {
        // given
        Volunteer volunteer1 = Volunteer.createDefault(OAuthProvider.NAVER, "1234");
        Volunteer volunteer2 = Volunteer.createDefault(OAuthProvider.NAVER, "1234");
        Volunteer volunteer3 = Volunteer.createDefault(OAuthProvider.NAVER, "1234");
        Volunteer volunteer4 = Volunteer.createDefault(OAuthProvider.NAVER, "1234");
        volunteer3.markAsDeleted();
        volunteer4.markAsDeleted();

        volunteerRepository.save(volunteer1);
        volunteerRepository.save(volunteer2);
        volunteerRepository.save(volunteer3);
        volunteerRepository.save(volunteer4);

        // when
        List<Volunteer> volunteers = volunteerQueryService.getAllByIds(
                List.of(volunteer1.getId(), volunteer2.getId(), volunteer3.getId(),
                        volunteer4.getId(), UUID.randomUUID()
                ));

        // then
        assertThat(volunteers).hasSize(2);
    }

    @DisplayName("봉사자 ID로 봉사자가 존재하는지 확인할 수 있다 (service)")
    @Test
    void validateVolunteerExists() {
        //given
        Volunteer volunteer = Volunteer.createDefault(OAuthProvider.NAVER, "1234");
        volunteerRepository.save(volunteer);

        //when & then
        assertDoesNotThrow(() ->
                volunteerQueryService.validateVolunteerExists(volunteer.getId())
        );
    }

    @DisplayName("존재하지 않는 봉사자 ID를 검증할 수 있다. (service)")
    @Test
    void validateNonExistentVolunteer() {
        //given
        UUID nonExistsVolunteerId = UUID.randomUUID();

        //when & then
        BadRequestException exception = assertThrows(BadRequestException.class, () ->
                volunteerQueryService.validateVolunteerExists(nonExistsVolunteerId)
        );

        assertEquals(NOT_EXISTS_VOLUNTEER.getMessage(), exception.getMessage());
    }

}
