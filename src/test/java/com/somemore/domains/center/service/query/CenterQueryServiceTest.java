package com.somemore.domains.center.service.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.somemore.domains.center.domain.Center;
import com.somemore.domains.center.repository.center.CenterRepository;
import com.somemore.global.exception.BadRequestException;
import com.somemore.global.exception.ExceptionMessage;
import com.somemore.support.IntegrationTestSupport;
import jakarta.transaction.Transactional;
import java.util.UUID;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Transactional
class CenterQueryServiceTest extends IntegrationTestSupport {

    @Autowired
    private CenterQueryService centerQueryService;

    @Autowired
    private CenterRepository centerRepository;

    @DisplayName("존재하지 않는 기관 ID를 검증할 수 있다.")
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
                "http://example.com"
        );
        Center savedCenter = centerRepository.save(center);

        // when
        ThrowableAssert.ThrowingCallable callable = () -> centerQueryService.validateCenterExists(
                savedCenter.getId());

        // then
        assertThatCode(callable).doesNotThrowAnyException();
    }

    @DisplayName("기관 Id로 기관명을 조회할 수 있다. (service)")
    @Test
    void getNameById() {
        // given
        Center center = createCenter();
        Center foundCenter = centerRepository.save(center);

        // when
        String foundName = centerQueryService.getNameById(foundCenter.getId());

        // then
        assertThat(foundName).isEqualTo("기본 기관 이름");
    }

    @DisplayName("존재하지 않는 기관 id로 기관명 조회 시 예외가 발생한다. (service)")
    @Test
    void getNameByNonExistentId() {
        // given
        // when
        ThrowableAssert.ThrowingCallable callable = () -> centerQueryService.getNameById(
                UUID.randomUUID());

        // then
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(callable)
                .withMessage(ExceptionMessage.NOT_EXISTS_CENTER.getMessage());
    }

    private Center createCenter() {
        return Center.create(
                "기본 기관 이름",
                "010-1234-5678",
                "http://example.com/image.jpg",
                "기관 소개 내용",
                "http://example.com"
        );
    }
}
