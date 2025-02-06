package com.somemore.center.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "prefer_item")
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
