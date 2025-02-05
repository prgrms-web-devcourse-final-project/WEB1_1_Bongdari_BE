package com.somemore.domains.interestcenter.service;

import static com.somemore.global.exception.ExceptionMessage.DUPLICATE_INTEREST_CENTER;
import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_CENTER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.somemore.center.domain.NEWCenter;
import com.somemore.center.repository.NEWCenterRepository;
import com.somemore.domains.interestcenter.domain.InterestCenter;
import com.somemore.domains.interestcenter.dto.request.RegisterInterestCenterRequestDto;
import com.somemore.domains.interestcenter.dto.response.RegisterInterestCenterResponseDto;
import com.somemore.domains.interestcenter.repository.InterestCenterRepository;
import com.somemore.domains.interestcenter.usecase.RegisterInterestCenterUseCase;
import com.somemore.global.exception.DuplicateException;
import com.somemore.global.exception.NoSuchElementException;
import com.somemore.support.IntegrationTestSupport;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class RegisterInterestCenterServiceTest extends IntegrationTestSupport {

    @Autowired
    private RegisterInterestCenterUseCase registerInterestCenter;

    @Autowired
    private InterestCenterRepository interestCenterRepository;

    @Autowired
    private NEWCenterRepository centerRepository;

    @Autowired
    private EntityManager em;

    private NEWCenter center;

    @BeforeEach
    void setUp() {
        center = createCenter();
        centerRepository.save(center);
    }

    @DisplayName("봉사자는 관심 기관을 등록할 수 있다.")
    @Test
    void RegisterInterestCenter() {
        // given
        UUID volunteerId = UUID.randomUUID();
        UUID centerId = center.getId();
        RegisterInterestCenterRequestDto requestDto = new RegisterInterestCenterRequestDto(
                centerId);

        // when
        RegisterInterestCenterResponseDto responseDto = registerInterestCenter.registerInterestCenter(
                volunteerId, requestDto);

        // then
        Optional<InterestCenter> result = interestCenterRepository.findById(responseDto.id());
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(responseDto.id());
        assertThat(result.get().getVolunteerId()).isEqualTo(volunteerId);
        assertThat(result.get().getCenterId()).isEqualTo(centerId);
    }

    @DisplayName("이미 관심 표시한 기관에 관심 표시를 시도하면 예외를 던져준다.")
    @Test
    void registerInterestCenter_WithDuplicateCenterId_ShouldThrowException() {
        // given
        UUID volunteerId = UUID.randomUUID();
        UUID centerId = center.getId();
        RegisterInterestCenterRequestDto requestDto = new RegisterInterestCenterRequestDto(
                centerId);

        registerInterestCenter.registerInterestCenter(volunteerId, requestDto);

        // when
        // then
        assertThatThrownBy(
                () -> registerInterestCenter.registerInterestCenter(volunteerId, requestDto))
                .isInstanceOf(DuplicateException.class)
                .hasMessage(DUPLICATE_INTEREST_CENTER.getMessage());
    }


    @DisplayName("존재하지 않는 기관 Id로 관심 기관 등록 시 예외가 발생한다.")
    @Test
    void registerInterestCenter_WithInvalidCenterId_ShouldThrowException() {
        // given
        UUID volunteerId = UUID.randomUUID();
        UUID invalidCenterId = UUID.randomUUID();
        RegisterInterestCenterRequestDto requestDto = new RegisterInterestCenterRequestDto(
                invalidCenterId);

        // when
        // then
        assertThatThrownBy(
                () -> registerInterestCenter.registerInterestCenter(volunteerId, requestDto))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage(NOT_EXISTS_CENTER.getMessage());
    }

    private NEWCenter createCenter() {
        return NEWCenter.createDefault(UUID.randomUUID());
    }
}
