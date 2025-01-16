package com.somemore.domains.center.service.command;

import com.somemore.domains.center.domain.Center;
import com.somemore.domains.center.domain.PreferItem;
import com.somemore.domains.center.dto.request.PreferItemCreateRequestDto;
import com.somemore.domains.center.repository.center.CenterRepository;
import com.somemore.domains.center.repository.preferitem.PreferItemJpaRepository;
import com.somemore.domains.center.repository.preferitem.PreferItemRepository;
import com.somemore.global.exception.BadRequestException;
import com.somemore.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.somemore.global.exception.ExceptionMessage.UNAUTHORIZED_PREFER_ITEM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.catchThrowable;

class DeletePreferItemServiceTest extends IntegrationTestSupport {

    @Autowired
    private CreatePreferItemService createPreferItemService;

    @Autowired
    private DeletePreferItemService deletePreferItemService;

    @Autowired
    private PreferItemRepository preferItemRepository;

    @Autowired
    private PreferItemJpaRepository preferItemJpaRepository;

    @Autowired
    private CenterRepository centerRepository;

    @DisplayName("기관은 자신의 선호 물품을 삭제할 수 있다.")
    @Test
    void deletePreferItem() {
        //given
        Center center = createCenter();
        String itemName = "어린이 도서";
        PreferItemCreateRequestDto createRequestDto = new PreferItemCreateRequestDto(itemName);
        createPreferItemService.createPreferItem(center.getId(), createRequestDto);

        PreferItem savedItem = preferItemJpaRepository.findAll().stream()
                .filter(item -> item.getCenterId().equals(center.getId())
                        && item.getItemName().equals(itemName))
                .findFirst()
                .orElseThrow(() -> new AssertionError("저장된 선호물품을 찾을 수 없습니다."));

        //when
        deletePreferItemService.deletePreferItem(center.getId(), savedItem.getId());

        //then
        List<PreferItem> remainingItems = preferItemJpaRepository.findAll().stream()
                .filter(item -> item.getCenterId().equals(center.getId()))
                .toList();

        assertThat(remainingItems).isEmpty();
    }

    @DisplayName("선호 물품을 등록한 기관이 아니라면 선호 물품을 삭제할 수 없다")
    @Test
    void deletePreferItemUnauthorized() {
        //given
        Center center1 = createCenter();
        Center center2 = createCenter();
        String itemName = "어린이 도서";
        PreferItemCreateRequestDto createRequestDto = new PreferItemCreateRequestDto(itemName);
        createPreferItemService.createPreferItem(center1.getId(), createRequestDto);

        PreferItem savedItem = preferItemJpaRepository.findAll().stream()
                .filter(item -> item.getCenterId().equals(center1.getId())
                        && item.getItemName().equals(itemName))
                .findFirst()
                .orElseThrow(() -> new AssertionError("저장된 선호물품을 찾을 수 없습니다."));

        // when
        Throwable thrown = catchThrowable(() -> deletePreferItemService.deletePreferItem(center2.getId(), savedItem.getId()));

        // then
        assertThat(thrown)
                .isInstanceOf(BadRequestException.class)
                .hasMessage(UNAUTHORIZED_PREFER_ITEM.getMessage());
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
