package com.somemore.domains.interestcenter.repository;

import com.somemore.domains.interestcenter.domain.InterestCenter;
import com.somemore.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class InterestNEWCenterRepositoryImplTest extends IntegrationTestSupport {

    @Autowired
    private InterestCenterRepository interestCenterRepository;

    @DisplayName("봉사자 아이디와 기관 아이디로 관심 기관을 조회할 수 있다.")
    @Test
    void findByVolunteerIdAndCenterId() {
        // given
        UUID centerId = UUID.randomUUID();
        UUID volunteerId = UUID.randomUUID();

        InterestCenter interestCenter = InterestCenter.create(volunteerId, centerId);

        interestCenterRepository.save(interestCenter);

        // when
        Optional<InterestCenter> result = interestCenterRepository.findByVolunteerIdAndCenterId(
                volunteerId, centerId);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getCenterId()).isEqualTo(centerId);
        assertThat(result.get().getVolunteerId()).isEqualTo(volunteerId);
    }

}
