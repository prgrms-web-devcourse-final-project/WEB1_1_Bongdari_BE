package com.somemore.center.service.query;

import com.somemore.IntegrationTestSupport;
import com.somemore.center.domain.Center;
import com.somemore.center.domain.PreferItem;
import com.somemore.center.dto.response.CenterProfileResponseDto;
import com.somemore.center.repository.CenterRepository;
import com.somemore.center.repository.PreferItemRepository;
import com.somemore.global.exception.BadRequestException;
import com.somemore.global.exception.ExceptionMessage;
import jakarta.transaction.Transactional;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@Transactional
class CenterQueryServiceTest extends IntegrationTestSupport {

    @Autowired
    private CenterQueryService centerQueryService;

    @Autowired
    private CenterRepository centerRepository;
    
    @Autowired
    private PreferItemRepository preferItemRepository;

    @DisplayName("기관 Id로 기관 프로필을 조회할 수 있다. (service)")
    @Test
    void getCenterProfileById() {
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
        Center foundCenter = centerRepository.save(center);

        PreferItem preferItem = PreferItem.create(foundCenter.getId(), "어린이 동화책");
        PreferItem preferItem1 = PreferItem.create(foundCenter.getId(), "간식");
        preferItemRepository.saveAll(List.of(preferItem, preferItem1));

        // when
        CenterProfileResponseDto responseDto = centerQueryService.getCenterProfileByCenterId(foundCenter.getId());

        // then
        assertThat(responseDto)
                .extracting(
                        "centerId",
                        "name",
                        "contactNumber",
                        "imgUrl",
                        "introduce",
                        "homepageLink"
                )
                .containsExactly(
                        foundCenter.getId(),
                        "기본 기관 이름",
                        "010-1234-5678",
                        "http://example.com/image.jpg",
                        "기관 소개 내용",
                        "http://example.com"
                );

        assertThat(responseDto.preferItems())
                .hasSize(2)
                .extracting("itemName")
                .containsExactly("어린이 동화책", "간식");
    }


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

    @DisplayName("센터의 id로 name을 조회한다.")
    @Test
    void getNameById() {

        //given
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

        //when
        String name = centerQueryService.getNameById(savedCenter.getId());

        //then
        assertThat(name).isEqualTo("기본 기관 이름");

    }

}
