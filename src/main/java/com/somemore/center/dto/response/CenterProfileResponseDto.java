package com.somemore.center.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.center.repository.record.CenterProfileDto;
import com.somemore.domains.center.dto.response.PreferItemResponseDto;
import com.somemore.user.repository.usercommonattribute.record.UserProfileDto;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public record CenterProfileResponseDto(
        UUID id,
        UUID userId,
        String homepageUrl,
        String name,
        String contactNumber,
        String imgUrl,
        String introduce,
        List<PreferItemResponseDto> preferItems
) {
    public static CenterProfileResponseDto of(CenterProfileDto centerProfileDto, UserProfileDto userProfileDto, List<PreferItemResponseDto> preferItems) {
        return CenterProfileResponseDto.builder()
                .id(centerProfileDto.id())
                .userId(centerProfileDto.userId())
                .homepageUrl(centerProfileDto.homepageUrl())
                .name(userProfileDto.name())
                .contactNumber(userProfileDto.contactNumber())
                .imgUrl(userProfileDto.imgUrl())
                .introduce(userProfileDto.introduce())
                .preferItems(preferItems)
                .build();
    }
}
