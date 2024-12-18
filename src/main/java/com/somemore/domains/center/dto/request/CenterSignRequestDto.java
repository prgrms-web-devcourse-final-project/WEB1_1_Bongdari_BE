package com.somemore.domains.center.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@JsonNaming(SnakeCaseStrategy.class)
@Builder
public record CenterSignRequestDto(
        @Schema(description = "기관 아이디", example = "somemore")
        String accountId,
        @Schema(description = "기관 패스워드", example = "password1234")
        String accountPassword
) {

}
