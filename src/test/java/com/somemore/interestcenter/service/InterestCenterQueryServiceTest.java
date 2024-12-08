package com.somemore.interestcenter.service;

import com.somemore.IntegrationTestSupport;
import com.somemore.center.domain.Center;
import com.somemore.center.repository.center.CenterJpaRepository;
import com.somemore.interestcenter.domain.InterestCenter;
import com.somemore.interestcenter.dto.response.InterestCentersResponseDto;
import com.somemore.interestcenter.repository.InterestCenterJpaRepository;
import com.somemore.interestcenter.repository.InterestCenterRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class InterestCenterQueryServiceTest extends IntegrationTestSupport {


    @Autowired
    private InterestCenterQueryService interestCenterQueryService;

    @Autowired
    private InterestCenterRepository interestCenterRepository;

    @Autowired
    private InterestCenterJpaRepository interestCenterJpaRepository;

    @Autowired
    private CenterJpaRepository centerJpaRepository;

    @DisplayName("봉사자 ID로 관심 센터 정보를 조회할 수 있다.")
    @Test
    void getInterestCenters() {
        // given
        UUID volunteerId = UUID.randomUUID();

        Center center = createCenter();
        Center center1 = createCenter();
        Center center2 = createCenter();
        centerJpaRepository.saveAll(List.of(center, center1, center2));

        InterestCenter interestCenter = createInterestCenter(volunteerId, center.getId());
        InterestCenter interestCenter1 = createInterestCenter(volunteerId, center1.getId());
        InterestCenter interestCenter2 = createInterestCenter(volunteerId, center2.getId());
        interestCenterJpaRepository.saveAll(List.of(interestCenter, interestCenter1, interestCenter2));

        // when
        List<InterestCentersResponseDto> result = interestCenterQueryService.getInterestCenters(volunteerId);

        // then
        assertThat(result)
                .hasSize(3)
                .extracting("centerId")
                .containsExactlyInAnyOrder(center.getId(), center1.getId(), center2.getId());

        assertThat(result)
                .extracting("centerName")
                .containsExactlyInAnyOrder("기본 기관 이름", "기본 기관 이름", "기본 기관 이름");

        assertThat(result)
                .extracting("imgUrl")
                .containsExactlyInAnyOrder("http://image.jpg", "http://image.jpg", "http://image.jpg");
    }

    @DisplayName("봉사자의 관심 센터가 없을 경우 빈 리스트를 반환한다.")
    @Test
    void getInterestCenters_ReturnsEmptyList_WhenNoInterestCenters() {
        // given
        UUID volunteerId = UUID.randomUUID();

        Center center = createCenter();
        centerJpaRepository.save(center);

        InterestCenter unrelatedInterestCenter = createInterestCenter(UUID.randomUUID(), center.getId());
        interestCenterJpaRepository.save(unrelatedInterestCenter);

        // when
        List<InterestCentersResponseDto> result = interestCenterQueryService.getInterestCenters(volunteerId);

        // then
        assertThat(result).isEmpty();
    }


    private Center createCenter() {
        return Center.create(
                "기본 기관 이름",
                "010-1234-5678",
                "http://image.jpg",
                "기관 소개 내용",
                "http://example.com",
                "account123",
                "password123"
        );
    }

    private InterestCenter createInterestCenter(UUID volunteerId, UUID centerId) {
        return InterestCenter.builder()
                .volunteerId(volunteerId)
                .centerId(centerId)
                .build();
    }
}