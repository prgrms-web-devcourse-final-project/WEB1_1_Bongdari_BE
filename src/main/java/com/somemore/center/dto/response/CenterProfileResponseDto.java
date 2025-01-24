package com.somemore.center.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.center.repository.record.CenterProfileDto;
import com.somemore.domains.center.dto.response.PreferItemResponseDto;
import com.somemore.user.repository.usercommonattribute.record.UserProfileDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public record CenterProfileResponseDto(

        @Schema(description = "기관 ID", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,

        @Schema(description = "유저 ID", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID userId,

        @Schema(description = "기관 홈페이지 링크", example = "https://fitnesscenter.com")
        String homepageUrl,

        @Schema(description = "기관 이름", example = "서울 도서관")
        String name,

        @Schema(description = "연락처", example = "010-xxxx-xxxx")
        String contactNumber,

        @Schema(description = "기관 프로필 이미지 URL", example = "https://example.com/images/center.jpg")
        String imgUrl,

        @Schema(description = "기관 소개", example = "저희 도서관은 유명해요")
        String introduce,

        @Schema(description = "선호 물품 리스트")
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
