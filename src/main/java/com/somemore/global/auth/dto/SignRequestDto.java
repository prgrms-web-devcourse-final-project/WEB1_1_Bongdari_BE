package com.somemore.global.auth.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@JsonNaming(SnakeCaseStrategy.class)
@Builder
public record SignRequestDto(
        @Schema(description = "ID", example = "somemore")
        String accountId,
        @Schema(description = "PW", example = "password1234")
        String accountPassword
) {

}
