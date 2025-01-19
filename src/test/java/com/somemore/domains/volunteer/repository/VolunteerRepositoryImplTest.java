package com.somemore.domains.volunteer.repository;

import com.somemore.domains.volunteer.domain.Volunteer;
import com.somemore.domains.volunteer.domain.VolunteerDetail;
import com.somemore.domains.volunteer.dto.request.VolunteerRegisterRequestDto;
import com.somemore.domains.volunteer.repository.mapper.VolunteerOverviewForRankingByHours;
import com.somemore.domains.volunteer.repository.mapper.VolunteerSimpleInfo;
import com.somemore.support.IntegrationTestSupport;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.somemore.global.auth.oauth.domain.OAuthProvider.NAVER;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class VolunteerRepositoryImplTest extends IntegrationTestSupport {

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private VolunteerDetailRepository volunteerDetailRepository;
    
    @DisplayName("봉사자의 id로 닉네임을 조회한다.")
    @Test
    void findNicknameById() {
        String oAuthId = "example-oauth-id";
        Volunteer volunteer = Volunteer.createDefault(NAVER, oAuthId);
        volunteerRepository.save(volunteer);

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

        String oAuthId = "example-oauth-id";
        Volunteer volunteer = Volunteer.createDefault(NAVER, oAuthId);
        volunteerRepository.save(volunteer);

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
        String oAuthId = "example-oauth-id";
        Volunteer volunteer = Volunteer.createDefault(NAVER, oAuthId);
        volunteerRepository.save(volunteer);
        // when
        Optional<Volunteer> foundVolunteer = volunteerRepository.findByOauthId(oAuthId);

        // then
        assertThat(foundVolunteer).isPresent();
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
        assertThat(rankings.get(0).totalVolunteerHours()).isGreaterThan(
                rankings.get(1).totalVolunteerHours());
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
        assertThat(rankings.get(0).totalVolunteerHours()).isGreaterThan(
                rankings.get(1).totalVolunteerHours());
    }

    @DisplayName("아이디 리스트로 봉사자를 조회할 수있다.")
    @Test
    void findAllByIds() {
        // given
        Volunteer volunteer1 = Volunteer.createDefault(NAVER, "1234");
        Volunteer volunteer2 = Volunteer.createDefault(NAVER, "1234");
        Volunteer volunteer3 = Volunteer.createDefault(NAVER, "1234");
        Volunteer volunteer4 = Volunteer.createDefault(NAVER, "1234");
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

    @DisplayName("아이디 리스트로 봉사자 간단 정보를 조회할 수 있다.")
    @Test
    void findSimpleInfoByIds() {
        // given
        List<UUID> ids = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            Volunteer volunteerTest = volunteerRepository.save(Volunteer.createDefault(NAVER, "naver"));
            String name = "name" + i;
            VolunteerRegisterRequestDto dto = createVolunteerRegisterRequestDto(name);
            volunteerDetailRepository.save(VolunteerDetail.of(dto, volunteerTest.getId()));
            ids.add(volunteerTest.getId());
        }

        // when
        List<VolunteerSimpleInfo> simpleInfo = volunteerRepository.findSimpleInfoByIds(ids);

        // then
        assertThat(simpleInfo).hasSize(4);
    }

    @DisplayName("봉사자 ID로 봉사자가 존재하는지 확인할 수 있다.")
    @Test
    void existsVolunteerById() {
        //given
        Volunteer existsVolunteer = volunteerRepository.save(Volunteer.createDefault(NAVER, "naver"));
        volunteerRepository.save(existsVolunteer);

        //when
        Boolean isExist = volunteerRepository.existsByVolunteerId(existsVolunteer.getId());

        //then
        AssertionsForClassTypes.assertThat(isExist).isTrue();
    }

    @DisplayName("존재하지 않는 봉사자 ID를 검증할 수 있다.")
    @Test
    void notExistsVolunteerById() {
        //given
        UUID nonExistentId = UUID.randomUUID();

        //when
        Boolean isExist = volunteerRepository.existsByVolunteerId(nonExistentId);

        //then
        AssertionsForClassTypes.assertThat(isExist).isFalse();
    }

    private void createVolunteerAndUpdateVolunteerStats(int i) {
        Volunteer volunteer = Volunteer.createDefault(NAVER, "oauth-id-" + i);
        volunteer.updateVolunteerStats(i * 10, i);
        volunteerRepository.save(volunteer);
    }

    private static VolunteerRegisterRequestDto createVolunteerRegisterRequestDto(String name) {
        return new VolunteerRegisterRequestDto(
                NAVER, "naver", name, "email", "M", "1111", "1111",
                "010-0000-0000");
    }

}
