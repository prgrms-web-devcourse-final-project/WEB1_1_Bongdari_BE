package com.somemore.domains.volunteer.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.domains.volunteer.repository.mapper.VolunteerSimpleInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.UUID;

@Schema(description = "봉사자 간단 정보 응답 DTO")
@JsonNaming(SnakeCaseStrategy.class)
@Builder
public record VolunteerSimpleInfoResponseDto(
        @Schema(description = "봉사자 ID", example = "f5a8779a-bcc9-4fc5-b8a1-7b2a383054a9")
        UUID id,
        @Schema(description = "봉사자 이름", example = "홍길동")
        String name,
        @Schema(description = "봉사자 닉네임", example = "gil-dong")
        String nickname,
        @Schema(description = "봉사자 이메일", example = "hong@example.com")
        String email,
        @Schema(description = "봉사자 이미지 URL", example = "https://example.com/images/hong.jpg")
        String imgUrl
) {

    public static VolunteerSimpleInfoResponseDto from(VolunteerSimpleInfo volunteerSimpleInfo) {
        return VolunteerSimpleInfoResponseDto.builder()
                .id(volunteerSimpleInfo.id())
                .name(volunteerSimpleInfo.name())
                .nickname(volunteerSimpleInfo.nickname())
                .email(volunteerSimpleInfo.email())
                .imgUrl(volunteerSimpleInfo.imgUrl())
                .build();
    }
}
