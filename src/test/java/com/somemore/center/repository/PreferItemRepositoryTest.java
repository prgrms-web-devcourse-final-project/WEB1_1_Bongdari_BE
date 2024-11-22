package com.somemore.center.repository;

import com.somemore.IntegrationTestSupport;
import com.somemore.center.domain.PreferItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

class PreferItemRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private PreferItemRepository preferItemRepository;

    @DisplayName("기관의 id로 선호물품을 검색할 수 있다.")
    @Test
    void findByCenterId() {

        //given
        PreferItem preferItem = PreferItem.create(UUID.fromString("1a1a1a1a-1a1a-1a1a-1a1a-1a1a1a1a1a1"), "어린이 동화책");
        PreferItem preferItem1 = PreferItem.create(UUID.fromString("1a1a1a1a-1a1a-1a1a-1a1a-1a1a1a1a1a1"), "간식");
        PreferItem preferItem2 = PreferItem.create(UUID.fromString("1a1a1a1a-1a1a-1a1a-1a1a-1a1a1a1a1a2"), "수건");
        PreferItem preferItem3 = PreferItem.create(UUID.fromString("1a1a1a1a-1a1a-1a1a-1a1a-1a1a1a1a1a3"), "식재료");
        preferItemRepository.saveAll(List.of(preferItem, preferItem1, preferItem2, preferItem3));

        //when
        List<PreferItem> preferItems = preferItemRepository.findByCenterId(UUID.fromString("1a1a1a1a-1a1a-1a1a-1a1a-1a1a1a1a1a2"));

        //then
        assertThat(preferItems).hasSize(1)
                .extracting("centerId", "itemName")
                .containsExactlyInAnyOrder(
                        tuple(UUID.fromString("1a1a1a1a-1a1a-1a1a-1a1a-1a1a1a1a1a2"), "수건")
                );
    }

}
