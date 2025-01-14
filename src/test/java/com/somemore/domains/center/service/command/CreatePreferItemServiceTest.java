package com.somemore.domains.center.service.command;

import com.somemore.domains.center.domain.Center;
import com.somemore.domains.center.domain.PreferItem;
import com.somemore.domains.center.dto.request.PreferItemCreateRequestDto;
import com.somemore.domains.center.repository.center.CenterRepository;
import com.somemore.domains.center.repository.preferitem.PreferItemJpaRepository;
import com.somemore.domains.center.repository.preferitem.PreferItemRepository;
import com.somemore.global.exception.BadRequestException;
import com.somemore.support.IntegrationTestSupport;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class CreatePreferItemServiceTest extends IntegrationTestSupport {

    @Autowired
    private CreatePreferItemService createPreferItemService;

    @Autowired
    private PreferItemRepository preferItemRepository;

    @Autowired
    private PreferItemJpaRepository preferItemJpaRepository;

    @Autowired
    private CenterRepository centerRepository;

    @DisplayName("기관 아이디와 선호물품 이름을 받아 선호물품을 생성한다.")
    @Test
    void createPreferItem() {
        //given
        Center center = createCenter();
        centerRepository.save(center);
        String itemName = "어린이 도서";

        PreferItemCreateRequestDto requestDto = new PreferItemCreateRequestDto(
                itemName
        );

        //when
        createPreferItemService.createPreferItem(center.getId(), requestDto);

        //then
        PreferItem savedItem = preferItemJpaRepository.findAll().stream()
                .filter(item -> item.getCenterId().equals(center.getId())
                        && item.getItemName().equals(itemName))
                .findFirst()
                .orElseThrow(() -> new AssertionError("저장된 선호물품을 찾을 수 없습니다."));

        assertThat(savedItem.getCenterId()).isEqualTo(center.getId());
        assertThat(savedItem.getItemName()).isEqualTo(itemName);
    }

    @DisplayName("존재하지 않는 기관 아이디로 선호물품을 등록하려고 하면 예외를 발생시킨다.")
    @Test
    void createPreferItemThrowsExceptionWhenCenterDoesNotExist() {
        // given
        UUID invalidCenterId = UUID.randomUUID();
        String itemName = "어린이 도서";

        PreferItemCreateRequestDto requestDto = new PreferItemCreateRequestDto(
                itemName
        );

        // when & then
        assertThatThrownBy(() -> createPreferItemService.createPreferItem(invalidCenterId, requestDto))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("존재하지 않는 기관입니다.");
    }

    private Center createCenter() {
        Center center = Center.create(
                "기본 기관 이름",
                "010-1234-5678",
                "http://example.com/image.jpg",
                "기관 소개 내용",
                "http://example.com"
        );

        centerRepository.save(center);

        return center;
    }

}
