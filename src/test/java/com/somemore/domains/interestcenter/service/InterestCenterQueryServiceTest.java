package com.somemore.domains.interestcenter.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.somemore.center.domain.NEWCenter;
import com.somemore.center.repository.NEWCenterRepository;
import com.somemore.domains.interestcenter.domain.InterestCenter;
import com.somemore.domains.interestcenter.dto.response.InterestCentersResponseDto;
import com.somemore.domains.interestcenter.repository.InterestCenterRepository;
import com.somemore.support.IntegrationTestSupport;
import com.somemore.user.domain.UserCommonAttribute;
import com.somemore.user.domain.UserRole;
import com.somemore.user.repository.usercommonattribute.UserCommonAttributeRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class InterestCenterQueryServiceTest extends IntegrationTestSupport {

    @Autowired
    private InterestCenterQueryService interestCenterQueryService;

    @Autowired
    private InterestCenterRepository interestCenterRepository;

    @Autowired
    private NEWCenterRepository centerRepository;

    @Autowired
    private UserCommonAttributeRepository userCommonAttributeRepository;

    @DisplayName("봉사자 ID로 관심 센터 정보를 조회할 수 있다.")
    @Test
    void getInterestCenters() {
        // given
        UUID volunteerId = UUID.randomUUID();

        NEWCenter centerOne = createCenter();
        NEWCenter centerTwo = createCenter();
        centerRepository.save(centerOne);
        centerRepository.save(centerTwo);

        UserCommonAttribute centerOneInfo = createCenterUserInfo(centerOne.getUserId());
        UserCommonAttribute centerTwoInfo = createCenterUserInfo(centerTwo.getUserId());
        userCommonAttributeRepository.save(centerOneInfo);
        userCommonAttributeRepository.save(centerTwoInfo);

        InterestCenter interestOne = createInterestCenter(volunteerId, centerOne.getId());
        InterestCenter interestTwo = createInterestCenter(volunteerId, centerTwo.getId());
        interestCenterRepository.save(interestOne);
        interestCenterRepository.save(interestTwo);

        // when
        List<InterestCentersResponseDto> result = interestCenterQueryService.getInterestCenters(
                volunteerId);

        // then
        assertThat(result)
                .hasSize(2)
                .extracting("centerId")
                .containsExactlyInAnyOrder(centerOne.getId(), centerTwo.getId());
    }

    @DisplayName("봉사자의 관심 센터가 없을 경우 빈 리스트를 반환한다.")
    @Test
    void getInterestCenters_ReturnsEmptyList_WhenNoInterestCenters() {
        // given
        UUID volunteerId = UUID.randomUUID();

        // when
        List<InterestCentersResponseDto> result = interestCenterQueryService.getInterestCenters(
                volunteerId);

        // then
        assertThat(result).isEmpty();
    }

    @DisplayName("센터 ID로 봉사자 ID 목록을 조회할 수 있다.")
    @Test
    void getVolunteerIdsByCenterId() {
        // given
        UUID centerId = UUID.randomUUID();
        UUID volunteerId1 = UUID.randomUUID();
        UUID volunteerId2 = UUID.randomUUID();
        UUID volunteerId3 = UUID.randomUUID();

        InterestCenter interestCenter1 = createInterestCenter(volunteerId1, centerId);
        InterestCenter interestCenter2 = createInterestCenter(volunteerId2, centerId);
        InterestCenter interestCenter3 = createInterestCenter(volunteerId3, centerId);
        interestCenterRepository.save(interestCenter1);
        interestCenterRepository.save(interestCenter2);
        interestCenterRepository.save(interestCenter3);

        // when
        List<UUID> result = interestCenterQueryService.getVolunteerIdsByCenterId(centerId);

        // then
        assertThat(result)
                .hasSize(3)
                .containsExactlyInAnyOrder(volunteerId1, volunteerId2, volunteerId3);
    }

    @DisplayName("센터 ID에 등록된 봉사자가 없을 경우 빈 리스트를 반환한다.")
    @Test
    void getVolunteerIdsByCenterId_ReturnsEmptyList_WhenNoVolunteers() {
        // given
        UUID wrongCenterId = UUID.randomUUID();

        // when
        List<UUID> result = interestCenterQueryService.getVolunteerIdsByCenterId(wrongCenterId);

        // then
        assertThat(result).isEmpty();
    }

    private NEWCenter createCenter() {
        return NEWCenter.createDefault(UUID.randomUUID());
    }

    private InterestCenter createInterestCenter(UUID volunteerId, UUID centerId) {
        return InterestCenter.builder()
                .volunteerId(volunteerId)
                .centerId(centerId)
                .build();
    }

    private UserCommonAttribute createCenterUserInfo(UUID userId) {
        return UserCommonAttribute.createDefault(userId, UserRole.CENTER);
    }
}
