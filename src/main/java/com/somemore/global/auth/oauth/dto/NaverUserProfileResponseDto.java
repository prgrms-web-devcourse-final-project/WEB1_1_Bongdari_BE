package com.somemore.global.auth.oauth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record NaverUserProfileResponseDto(
        String resultcode, // 결과 코드
        String message,    // 결과 메시지
        Response response  // 응답 데이터
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
            String id // OAuth ID
    ) {
    }
}
