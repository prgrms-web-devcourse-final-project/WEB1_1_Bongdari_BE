package com.somemore.center.repository;

import static com.somemore.user.domain.UserRole.CENTER;
import static org.assertj.core.api.Assertions.assertThat;

import com.somemore.center.domain.NEWCenter;
import com.somemore.center.repository.record.CenterOverviewInfo;
import com.somemore.center.repository.record.CenterProfileDto;
import com.somemore.domains.center.domain.Center;
import com.somemore.support.IntegrationTestSupport;
import com.somemore.user.domain.UserCommonAttribute;
import com.somemore.user.domain.UserRole;
import com.somemore.user.repository.usercommonattribute.UserCommonAttributeRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class NEWCenterRepositoryImplTest extends IntegrationTestSupport {

    @Autowired
    private NEWCenterRepositoryImpl centerRepository;

    @Autowired
    private UserCommonAttributeRepository userCommonAttributeRepository;

    @DisplayName("유저 아이디로 기관을 등록할 수 있다.")
    @Test
    void saveVolunteerByUserId() {
        // given
        UUID userId = UUID.randomUUID();
        NEWCenter center = NEWCenter.createDefault(userId);

        // when
        centerRepository.save(center);

        // then
        NEWCenter centerByUserId = centerRepository.findByUserId(userId).orElseThrow();
        NEWCenter centerById = centerRepository.findById(center.getId()).orElseThrow();

        assertThat(center)
                .isEqualTo(centerByUserId)
                .isEqualTo(centerById);

    }

    @DisplayName("기관 아이디로 기관 프로필 정보를 가져올 수 있다.")
    @Test
    void findCenterProfileByUserId() {
        // given
        UUID userId = UUID.randomUUID();
        NEWCenter center = NEWCenter.createDefault(userId);
        centerRepository.save(center);

        // when
        Optional<CenterProfileDto> result = centerRepository.findCenterProfileById(center.getId());

        // then
        assertThat(result).isPresent();
        assertThat(result.get())
                .extracting("id", "userId")
                .containsExactly(center.getId(), userId);
    }

    @DisplayName("존재하지 않는 유저 아이디로 기관 정보 조회시 빈 값을 반환한다.")
    @Test
    void findCenterProfileByUserId_NoResult() {

        // given
        UUID nonExistentCenterId = UUID.randomUUID();

        // when
        Optional<CenterProfileDto> result = centerRepository.findCenterProfileById(
                nonExistentCenterId);

        // then
        assertThat(result).isEmpty();
    }

    @DisplayName("아이디로 기관 존재 유무를 조회할 수 있다.")
    @Test
    void existsById() {
        // given
        NEWCenter center = NEWCenter.createDefault(UUID.randomUUID());
        centerRepository.save(center);

        // when
        boolean result = centerRepository.existsById(center.getId());

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("아이디 리스트로 기관 기본 정보를 조회할 수 있다.")
    @Test
    void findUserIdsByIds() {
        // given
        NEWCenter centerOne = NEWCenter.createDefault(UUID.randomUUID());
        NEWCenter centerTwo = NEWCenter.createDefault(UUID.randomUUID());
        centerRepository.save(centerOne);
        centerRepository.save(centerTwo);

        UserCommonAttribute centerUserInfoOne = createCenterUserAttribute(centerOne.getUserId());
        UserCommonAttribute centerUserInfoTwo = createCenterUserAttribute(centerTwo.getUserId());
        userCommonAttributeRepository.save(centerUserInfoOne);
        userCommonAttributeRepository.save(centerUserInfoTwo);

        List<UUID> ids = List.of(centerOne.getId(), centerTwo.getId());

        // when
        List<CenterOverviewInfo> centerOverviewInfos = centerRepository.findOverviewInfosByIds(ids);

        // then
        assertThat(centerOverviewInfos)
                .isNotNull()
                .hasSize(2)
                .extracting(CenterOverviewInfo::centerId)
                .containsExactlyInAnyOrderElementsOf(ids);
    }

    @DisplayName("기관 Id로 기관명을 조회할 수 있다.")
    @Test
    void findNameById() {
        //given
        NEWCenter center = NEWCenter.createDefault(UUID.randomUUID());
        centerRepository.save(center);
        UserCommonAttribute centerUserInfo = createCenterUserAttribute(center.getUserId());
        userCommonAttributeRepository.save(centerUserInfo);

        //when
        String foundName = centerRepository.findNameById(center.getId());

        //then
        AssertionsForClassTypes.assertThat(foundName).isNotNull();
        AssertionsForClassTypes.assertThat(foundName).contains("기관");
    }

    @DisplayName("존재하지 않는 기관 id로 기관명 조회 시 null을 반환한다.")
    @Test
    void findNameByNonExistentId() {
        //given
        //when
        String foundName = centerRepository.findNameById(UUID.randomUUID());

        //then
        AssertionsForClassTypes.assertThat(foundName).isNull();
    }

    private UserCommonAttribute createCenterUserAttribute(UUID userId) {
        return UserCommonAttribute.createDefault(userId, CENTER);
    }
}
