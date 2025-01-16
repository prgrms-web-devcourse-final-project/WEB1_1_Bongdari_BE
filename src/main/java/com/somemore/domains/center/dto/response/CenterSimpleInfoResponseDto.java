package com.somemore.domains.center.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.domains.center.domain.Center;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.UUID;

@Builder
@JsonNaming(SnakeCaseStrategy.class)
@Schema(description = "기관 정보 응답 DTO")
public record CenterSimpleInfoResponseDto(
        @Schema(description = "기관 아이디", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,
        @Schema(description = "기관 이름", example = "환경 봉사 센터")
        String name
) {

    public static CenterSimpleInfoResponseDto from(Center center) {
        return CenterSimpleInfoResponseDto.builder()
                .id(center.getId())
                .name(center.getName())
                .build();
    }

    public static CenterSimpleInfoResponseDto of(UUID centerId, String name) {
        return CenterSimpleInfoResponseDto.builder()
                .id(centerId)
                .name(name)
                .build();
    }
}
