package com.somemore.center.service.query;

import com.somemore.IntegrationTestSupport;
import com.somemore.center.domain.Center;
import com.somemore.center.domain.PreferItem;
import com.somemore.center.dto.response.CenterOverviewInfoResponseDto;
import com.somemore.center.dto.response.CenterProfileResponseDto;
import com.somemore.center.repository.CenterJpaRepository;
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

import static org.assertj.core.api.Assertions.*;

@Transactional
class CenterQueryServiceTest extends IntegrationTestSupport {

    @Autowired
    private CenterQueryService centerQueryService;

    @Autowired
    private CenterRepository centerRepository;

    @Autowired
    private CenterJpaRepository centerJpaRepository;
    
    @Autowired
    private PreferItemRepository preferItemRepository;

    @DisplayName("기관 Id로 기관 프로필을 조회할 수 있다. (service)")
    @Test
    void getCenterProfileById() {
        // given
        Center center = createCenter();
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

    @DisplayName("기관 Id들로 기관의 오버뷰 정보를 조회할 수 있다.")
    @Test
    void getCenterOverviewsByIds() {
        //given
        Center center = createCenter();
        Center center1 = createCenter();
        Center center2 = createCenter();
        centerJpaRepository.saveAll(List.of(center, center1, center2));
        List<UUID> ids = List.of(center.getId(),center1.getId(), center2.getId());

        //when
        List<CenterOverviewInfoResponseDto> responseDtos = centerQueryService.getCenterOverviewsByIds(ids);

        //then
        assertThat(responseDtos)
                .isNotNull()
                .hasSize(3)
                .extracting("centerId", "centerName", "imgUrl")
                .containsExactlyInAnyOrder(
                        tuple(center.getId(), center.getName(), center.getImgUrl()),
                        tuple(center1.getId(), center1.getName(), center1.getImgUrl()),
                        tuple(center2.getId(), center2.getName(), center2.getImgUrl())
                );
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

    private Center createCenter() {
        return Center.create(
                "기본 기관 이름",
                "010-1234-5678",
                "http://example.com/image.jpg",
                "기관 소개 내용",
                "http://example.com",
                "account123",
                "password123"
        );
    }
}
