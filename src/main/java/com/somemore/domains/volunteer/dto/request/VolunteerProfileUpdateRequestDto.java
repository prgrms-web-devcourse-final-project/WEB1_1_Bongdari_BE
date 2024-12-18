package com.somemore.domains.volunteer.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record VolunteerProfileUpdateRequestDto(

        @Schema(description = "봉사자 닉네임", example = "making")
        @NotBlank(message = "닉네임은 필수 값입니다.")
        @Size(max = 10, message = "닉네임은 최대 10자까지 입력 가능합니다.")
        String nickname,

        @Schema(description = "봉사자 소개글", example = "저는 다양한 봉사활동에 관심이 많은 봉사자입니다.")
        @NotBlank(message = "소개글은 필수 값입니다.")
        @Size(max = 100, message = "소개글은 최대 100자까지 입력 가능합니다.")
        String introduce
) {
}