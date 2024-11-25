package com.somemore.auth.oauth.naver.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.auth.oauth.OAuthProvider;
import com.somemore.volunteer.dto.request.VolunteerRegisterRequestDto;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record NaverUserProfileResponseDto(
        String resultcode, // 결과 코드
        String message,    // 결과 메시지
        Response response  // 응답 데이터
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
            String id,             // 일련 번호
            String name,           // 이름
            String email,          // 이메일
            String gender,         // 성별 (F, M, U)
            String birthday,       // 생일 (MM-DD)
            String birthyear,      // 출생 연도
            String mobile          // 휴대 전화 번호
    ) {}

    public VolunteerRegisterRequestDto toVolunteerRegisterRequestDto() {
        return new VolunteerRegisterRequestDto(
                OAuthProvider.NAVER,
                this.response.id(),
                this.response.name(),
                this.response.email(),
                this.response.gender(),
                this.response.birthday(),
                this.response.birthyear(),
                this.response.mobile()
        );
    }
}
