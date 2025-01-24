package com.somemore.volunteer.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.user.domain.UserCommonAttribute;
import com.somemore.volunteer.repository.record.VolunteerNicknameAndId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.UUID;

@Schema(description = "봉사자 간단 정보 응답 DTO")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public record VolunteerInfoResponseDto(
        @Schema(description = "봉사자 ID", example = "f5a8779a-bcc9-4fc5-b8a1-7b2a383054a9")
        UUID id,
        @Schema(description = "봉사자 이름", example = "홍길동")
        String name,
        @Schema(description = "봉사자 닉네임", example = "gil-dong")
        String nickname,
        @Schema(description = "봉사자 연락처", example = "010-1234-5678")
        String contactNumber,
        @Schema(description = "봉사자 이미지 URL", example = "https://example.com/images/hong.jpg")
        String imgUrl
) {

    public static VolunteerInfoResponseDto of(VolunteerNicknameAndId volunteer, UserCommonAttribute userCommonAttribute) {
        return VolunteerInfoResponseDto.builder()
                .id(volunteer.id())
                .name(userCommonAttribute.getName())
                .nickname(volunteer.nickname())
                .contactNumber(userCommonAttribute.getContactNumber())
                .imgUrl(userCommonAttribute.getImgUrl())
                .build();
    }
}
