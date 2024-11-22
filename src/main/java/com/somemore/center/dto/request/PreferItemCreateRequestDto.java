package com.somemore.center.dto.request;


import com.somemore.center.domain.PreferItem;

import java.util.UUID;


public record PreferItemCreateRequestDto(

        UUID centerId,
        String itemName

) {
    public PreferItem createPreferItem() {
        return PreferItem.create(centerId, itemName);
    }

}
