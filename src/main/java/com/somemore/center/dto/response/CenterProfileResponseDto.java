package com.somemore.center.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.center.domain.Center;
import com.somemore.center.domain.PreferItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public record CenterProfileResponseDto(
        @Schema(description = "센터 ID", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID centerId,

        @Schema(description = "센터 이름", example = "서울 도서관")
        String name,

        @Schema(description = "연락처", example = "010-1234-5678")
        String contactNumber,

        @Schema(description = "센터 이미지 URL", example = "https://example.com/images/center.jpg")
        String imgUrl,

        @Schema(description = "센터 소개", example = "저희 도서관은 유명해요")
        String introduce,

        @Schema(description = "센터 홈페이지 링크", example = "https://fitnesscenter.com")
        String homepageLink,

        @Schema(description = "선호 물품 리스트")
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
