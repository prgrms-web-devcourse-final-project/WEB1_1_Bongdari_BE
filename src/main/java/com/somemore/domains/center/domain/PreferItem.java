package com.somemore.domains.center.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class PreferItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "center_id", nullable = false)
    private UUID centerId;

    @Column(name = "item_name", nullable = false)
    private String itemName;

    @Builder
    private PreferItem(UUID centerId, String itemName) {
        this.centerId = centerId;
        this.itemName = itemName;
    }

    public static PreferItem create(UUID centerId, String itemName) {
        return PreferItem.builder()
                .centerId(centerId)
                .itemName(itemName)
                .build();
    }
}
