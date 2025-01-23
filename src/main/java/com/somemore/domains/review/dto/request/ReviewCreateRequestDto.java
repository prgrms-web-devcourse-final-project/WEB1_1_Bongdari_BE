package com.somemore.domains.review.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.domains.review.domain.Review;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@JsonNaming(SnakeCaseStrategy.class)
@Builder
public record ReviewCreateRequestDto(
        @Schema(description = "봉사 지원 아이디", example = "1")
        @NotNull(message = "봉사 지원 아이디는 필수 값입니다.")
        Long volunteerApplyId,
        @Schema(description = "리뷰 제목", example = "내 인생 최고의 봉사 활동")
        @NotBlank(message = "리뷰 제목은 필수 값입니다.")
        String title,
        @Schema(description = "리뷰 내용", example = "담당자님도 정말 친절하였고 정말 보람찬 봉사였어요 <br>")
        @NotBlank(message = "리뷰 내용은 필수 값입니다.")
        String content
) {

    public Review toEntity(UUID volunteerId) {
        return Review.builder()
                .volunteerApplyId(volunteerApplyId)
                .volunteerId(volunteerId)
                .title(title)
                .content(content)
                .build();
    }
}
