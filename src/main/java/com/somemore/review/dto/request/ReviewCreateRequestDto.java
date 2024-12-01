package com.somemore.review.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.review.domain.Review;
import com.somemore.volunteerapply.domain.VolunteerApply;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public record ReviewCreateRequestDto(
        @Schema(description = "봉사 모집글 아이디", example = "1")
        @NotNull(message = "봉사 모집글 아이디는 필수 값입니다.")
        Long recruitBoardId,
        @Schema(description = "리뷰 제목", example = "내 인생 최고의 봉사 활동")
        @NotBlank(message = "리뷰 제목은 필수 값입니다.")
        String title,
        @Schema(description = "리뷰 내용", example = "담당자님도 정말 친절하였고 정말 보람찬 봉사였어요 <br>")
        @NotBlank(message = "리뷰 내용은 필수 값입니다.")
        String content
) {

    public Review toEntity(VolunteerApply apply, UUID volunteerId, String imgUrl) {
        return Review.builder()
                .volunteerApplyId(apply.getId())
                .volunteerId(volunteerId)
                .title(title)
                .content(content)
                .imgUrl(imgUrl)
                .build();
    }
}
