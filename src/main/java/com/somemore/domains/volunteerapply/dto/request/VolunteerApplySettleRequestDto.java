package com.somemore.domains.volunteerapply.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@JsonNaming(SnakeCaseStrategy.class)
@Builder
public record VolunteerApplySettleRequestDto(
        @Schema(description = "지원 아이디 리스트", example = "[1, 2, 3]")
        @NotNull(message = "지원 아이디 리스트는 필수 값입니다.")
        List<Long> ids
) {

}
