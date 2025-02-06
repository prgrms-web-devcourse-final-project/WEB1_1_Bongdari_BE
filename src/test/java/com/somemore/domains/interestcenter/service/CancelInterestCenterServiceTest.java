package com.somemore.domains.interestcenter.service;

import static com.somemore.global.exception.ExceptionMessage.CANNOT_CANCEL_DELETED_INTEREST_CENTER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.somemore.center.domain.NEWCenter;
import com.somemore.center.repository.NEWCenterRepository;
import com.somemore.domains.interestcenter.domain.InterestCenter;
import com.somemore.domains.interestcenter.repository.InterestCenterRepository;
import com.somemore.domains.interestcenter.usecase.CancelInterestCenterUseCase;
import com.somemore.global.exception.NoSuchElementException;
import com.somemore.support.IntegrationTestSupport;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class CancelInterestCenterServiceTest extends IntegrationTestSupport {

    @Autowired
    private CancelInterestCenterUseCase cancelInterestCenterUseCase;

    @Autowired
    private InterestCenterRepository interestCenterRepository;

    @Autowired
    private NEWCenterRepository centerRepository;

    private NEWCenter center;
    private UUID volunteerId;
    private InterestCenter interestCenter;

    @BeforeEach
    void setUp() {
        center = createCenter();
        centerRepository.save(center);

        volunteerId = UUID.randomUUID();
        interestCenter = createInterestCenter(volunteerId, center.getId());
        interestCenterRepository.save(interestCenter);
    }

    @DisplayName("봉사자는 기관에 대한 관심 표시를 취소할 수 있다.")
    @Test
    void CancelInterestCenter() {
        // given
        UUID centerId = center.getId();

        //when
        cancelInterestCenterUseCase.cancelInterestCenter(volunteerId, centerId);

        //then
        Optional<InterestCenter> findInterestCenter = interestCenterRepository.findById(
                interestCenter.getId());
        assertThat(findInterestCenter).isEmpty();
    }

    @DisplayName("관심 기관에 없는 기관을 취소하면 예외가 발생한다.")
    @Test
    void cancelInterestCenterWhenDoesNotExist() {
        // given
        NEWCenter newCenter = createCenter();
        centerRepository.save(newCenter);
        UUID wrongCenterId = newCenter.getId();

        // when
        // then
        assertThatThrownBy(
                () -> cancelInterestCenterUseCase.cancelInterestCenter(volunteerId, wrongCenterId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage(CANNOT_CANCEL_DELETED_INTEREST_CENTER.getMessage());
    }

    private NEWCenter createCenter() {
        return NEWCenter.createDefault(UUID.randomUUID());
    }

    private InterestCenter createInterestCenter(UUID volunteerId, UUID centerId) {
        return InterestCenter.create(volunteerId, centerId);
    }

}
