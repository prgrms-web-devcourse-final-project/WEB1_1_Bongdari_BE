package com.somemore.center.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.center.domain.Center;
import com.somemore.center.domain.PreferItem;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public record CenterProfileResponseDto(
        UUID centerId,
        String name,
        String contactNumber,
        String imgUrl,
        String introduce,
        String homepageLink,
        List<PreferItemResponseDto> preferItems
) {
    public static CenterProfileResponseDto of(Center center, List<PreferItemResponseDto> preferItemDtos) {
        return CenterProfileResponseDto.builder()
                .centerId(center.getId())
                .name(center.getName())
                .contactNumber(center.getContactNumber())
                .imgUrl(center.getImgUrl())
                .introduce(center.getIntroduce())
                .homepageLink(center.getHomepageLink())
                .preferItems(preferItemDtos)
                .build();
    }
}
