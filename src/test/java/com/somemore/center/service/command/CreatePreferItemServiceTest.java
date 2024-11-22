package com.somemore.center.service.command;

import com.somemore.IntegrationTestSupport;
import com.somemore.center.domain.Center;
import com.somemore.center.domain.PreferItem;
import com.somemore.center.dto.request.PreferItemCreateRequestDto;
import com.somemore.center.repository.CenterRepository;
import com.somemore.center.repository.PreferItemRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class CreatePreferItemServiceTest extends IntegrationTestSupport {

    @Autowired
    private CreatePreferItemService createPreferItemService;

    @Autowired
    private PreferItemRepository preferItemRepository;

    @Autowired
    private CenterRepository centerRepository;

    @DisplayName("기관 아이디와 선호물품 이름을 받아 선호물품을 생성한다.")
    @Test
    void createPreferItem() {
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
        centerRepository.save(center);
        String itemName = "어린이 도서";


        PreferItemCreateRequestDto requestDto = new PreferItemCreateRequestDto(
                center.getId(),
                itemName
        );

        //when
        createPreferItemService.createPreferItem(requestDto);

        //then
        PreferItem savedItem = preferItemRepository.findAll().stream()
                .filter(item -> item.getCenterId().equals(center.getId())
                        && item.getItemName().equals(itemName))
                .findFirst()
                .orElseThrow(() -> new AssertionError("저장된 선호물품을 찾을 수 없습니다."));

        assertThat(savedItem.getCenterId()).isEqualTo(center.getId());
        assertThat(savedItem.getItemName()).isEqualTo(itemName);
    }

}
