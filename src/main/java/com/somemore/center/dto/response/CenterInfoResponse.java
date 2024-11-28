package com.somemore.center.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.center.domain.Center;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import lombok.Builder;

@Builder
@JsonNaming(SnakeCaseStrategy.class)
@Schema(description = "기관 정보 응답 DTO")
public record CenterInfoResponse(
    @Schema(description = "기관 아이디", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID id,
    @Schema(description = "기관 이름", example = "환경 봉사 센터")
    String name
) {

    public static CenterInfoResponse from(Center center) {
        return CenterInfoResponse.builder()
            .id(center.getId())
            .name(center.getName())
            .build();
    }

    public static CenterInfoResponse of(UUID centerId, String name) {
        return CenterInfoResponse.builder()
            .id(centerId)
            .name(name)
            .build();
    }
}
