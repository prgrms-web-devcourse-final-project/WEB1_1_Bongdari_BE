package com.somemore.center.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

import com.somemore.center.domain.PreferItem;
import com.somemore.center.repository.preferitem.PreferItemJpaRepository;
import com.somemore.center.repository.preferitem.PreferItemRepository;
import com.somemore.support.IntegrationTestSupport;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class PreferItemRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private PreferItemRepository preferItemRepository;

    @Autowired
    private PreferItemJpaRepository preferItemJpaRepository;

    private UUID centerId = UUID.fromString("1a1a1a1a-1a1a-1a1a-1a1a-1a1a1a1a1a1");
    private UUID centerId1 = UUID.fromString("1a1a1a1a-1a1a-1a1a-1a1a-1a1a1a1a1a1");
    private UUID centerId2 = UUID.fromString("1a1a1a1a-1a1a-1a1a-1a1a-1a1a1a1a1a2");
    private UUID centerId3 = UUID.fromString("1a1a1a1a-1a1a-1a1a-1a1a-1a1a1a1a1a3");

    @DisplayName("기관의 id로 선호물품을 검색할 수 있다.")
    @Test
    void findByCenterId() {

        //given
        PreferItem preferItem = PreferItem.create(centerId, "어린이 동화책");
        PreferItem preferItem1 = PreferItem.create(centerId1, "간식");
        PreferItem preferItem2 = PreferItem.create(centerId2, "수건");
        PreferItem preferItem3 = PreferItem.create(centerId3, "식재료");
        preferItemJpaRepository.saveAll(List.of(preferItem, preferItem1, preferItem2, preferItem3));

        //when
        List<PreferItem> preferItems = preferItemRepository.findByCenterId(
                UUID.fromString("1a1a1a1a-1a1a-1a1a-1a1a-1a1a1a1a1a2"));

        //then
        assertThat(preferItems).hasSize(1)
                .extracting("centerId", "itemName")
                .containsExactlyInAnyOrder(
                        tuple(UUID.fromString("1a1a1a1a-1a1a-1a1a-1a1a-1a1a1a1a1a2"), "수건")
                );
    }

}
