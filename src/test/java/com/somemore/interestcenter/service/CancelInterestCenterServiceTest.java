package com.somemore.interestcenter.service;

import com.somemore.IntegrationTestSupport;
import com.somemore.center.domain.Center;
import com.somemore.center.repository.center.CenterRepository;
import com.somemore.global.exception.BadRequestException;
import com.somemore.interestcenter.domain.InterestCenter;
import com.somemore.interestcenter.dto.request.RegisterInterestCenterRequestDto;
import com.somemore.interestcenter.dto.response.RegisterInterestCenterResponseDto;
import com.somemore.interestcenter.repository.InterestCenterRepository;
import com.somemore.interestcenter.usecase.CancelInterestCenterUseCase;
import com.somemore.interestcenter.usecase.RegisterInterestCenterUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static com.somemore.global.exception.ExceptionMessage.CANNOT_CANCEL_DELETED_INTEREST_CENTER;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
class CancelInterestCenterServiceTest extends IntegrationTestSupport {

    @Autowired
    private CancelInterestCenterUseCase cancelInterestCenterUseCase;

    @Autowired
    private RegisterInterestCenterUseCase registerInterestCenterUseCase;

    @Autowired
    private InterestCenterRepository interestCenterRepository;

    @Autowired
    private CenterRepository centerRepository;

    @DisplayName("봉사자는 기관에 대한 관심 표시를 취소할 수 있다.")
    @Test
    void CancelInterestCenter() {
        //given
        Center center = createCenter();
        UUID volunteerId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        UUID centerId = center.getId();
        RegisterInterestCenterRequestDto requestDto = new RegisterInterestCenterRequestDto(volunteerId, centerId);

        RegisterInterestCenterResponseDto responseDto = registerInterestCenterUseCase.registerInterestCenter(requestDto);

        InterestCenter savedInterestCenter = interestCenterRepository.findById(responseDto.id())
                .orElseThrow(() -> new IllegalStateException("등록된 관심 기관이 없습니다."));
        assertEquals(savedInterestCenter.getId(), responseDto.id());

        //when
        cancelInterestCenterUseCase.cancelInterestCenter(responseDto.id());

        //then
        Optional<InterestCenter> deletedInterestCenterOptional = interestCenterRepository.findById(responseDto.id());
        assertTrue(deletedInterestCenterOptional.isEmpty());

    }

    @DisplayName("이미 삭제된 관심 기관을 다시 취소하려 하면 예외가 발생한다.")
    @Test
    void cancelInterestCenter_AlreadyDeleted_ShouldThrowException() {
        //given
        Center center = createCenter();
        UUID volunteerId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        UUID centerId = center.getId();
        RegisterInterestCenterRequestDto requestDto = new RegisterInterestCenterRequestDto(volunteerId, centerId);
        RegisterInterestCenterResponseDto responseDto = registerInterestCenterUseCase.registerInterestCenter(requestDto);
        cancelInterestCenterUseCase.cancelInterestCenter(responseDto.id());

        //when, then
        long interestCenterId = responseDto.id();
        assertThrows(BadRequestException.class,
                () -> cancelInterestCenterUseCase.cancelInterestCenter(interestCenterId),
                CANNOT_CANCEL_DELETED_INTEREST_CENTER.getMessage()
        );
    }

    private Center createCenter() {
        Center center = Center.create(
                "기본 기관 이름",
                "010-1234-5678",
                "http://example.com/image.jpg",
                "기관 소개 내용",
                "http://example.com",
                "account123",
                "password123"
        );

        centerRepository.save(center);

        return center;
    }

}
