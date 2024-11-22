package com.somemore.center.service.query;

import com.somemore.IntegrationTestSupport;
import com.somemore.center.domain.Center;
import com.somemore.center.repository.CenterRepository;
import com.somemore.global.exception.BadRequestException;
import com.somemore.global.exception.ExceptionMessage;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class CenterQueryServiceTest extends IntegrationTestSupport {

    @Autowired
    private CenterQueryService centerQueryService;

    @Autowired
    private CenterRepository centerRepository;

    @DisplayName("존재하지 않는 기관 ID로 조회 시 예외가 발생한다")
    @Test
    void validateNonExistentCenter() {
        // given
        UUID nonExistentId = UUID.randomUUID();

        // when
        ThrowableAssert.ThrowingCallable throwingCallable = () ->
                centerQueryService.validateCenterExists(nonExistentId);

        // then
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(throwingCallable)
                .withMessage(ExceptionMessage.NOT_EXISTS_CENTER.getMessage());
    }

    @DisplayName("존재하는 기관 ID로 조회 시 예외가 발생하지 않는다")
    @Test
    void validateExistingCenter() {
        // given
        Center center = Center.create(
                "기본 기관 이름",
                "010-1234-5678",
                "http://example.com/image.jpg",
                "기관 소개 내용",
                "http://example.com",
                "account123",
                "password123"
        );
        Center savedCenter = centerRepository.save(center);

        // when
        ThrowableAssert.ThrowingCallable callable = () -> centerQueryService.validateCenterExists(savedCenter.getId());

        // then
        assertThatCode(callable).doesNotThrowAnyException();
    }

}
