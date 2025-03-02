package com.somemore.domains.review.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.domains.review.domain.Review;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@JsonNaming(SnakeCaseStrategy.class)
@Schema(description = "리뷰 상세 조회 응답 DTO")
public record ReviewDetailResponseDto(
        @Schema(description = "리뷰 ID", example = "123")
        Long id,
        @Schema(description = "봉사 지원 ID", example = "1")
        Long volunteerApplyId,
        @Schema(description = "모집글 ID", example = "1")
        Long recruitBoardId,
        @Schema(description = "봉사자(작성자) ID", example = "9b1deb4d-3b7d-4bad-9bdd-2b0d7b3dcb6d")
        UUID volunteerId,
        @Schema(description = "리뷰 제목", example = "제 인생 최고의 봉사활동")
        String title,
        @Schema(description = "리뷰 내용", example = "정말 유익했습니다. 더보기..")
        String content,
        @Schema(description = "작성 일자", example = "2024-12-01T09:00:00", type = "string")
        LocalDateTime createdAt,
        @Schema(description = "수정 일자", example = "2024-12-01T09:00:00", type = "string")
        LocalDateTime updatedAt
) {

    public static ReviewDetailResponseDto of(Review review, Long recruitBoardId) {
        return ReviewDetailResponseDto.builder()
                .id(review.getId())
                .volunteerApplyId(review.getVolunteerApplyId())
                .recruitBoardId(recruitBoardId)
                .volunteerId(review.getVolunteerId())
                .title(review.getTitle())
                .content(review.getContent())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }

}
