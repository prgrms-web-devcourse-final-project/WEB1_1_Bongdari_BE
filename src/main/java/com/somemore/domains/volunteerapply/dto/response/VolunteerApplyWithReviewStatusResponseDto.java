package com.somemore.domains.volunteerapply.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.domains.volunteerapply.domain.ApplyStatus;
import com.somemore.domains.volunteerapply.domain.VolunteerApply;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonNaming(SnakeCaseStrategy.class)
@Builder
public record VolunteerApplyWithReviewStatusResponseDto(
        @Schema(description = "봉사 지원 ID", example = "1")
        Long id,
        @Schema(description = "봉사자 UUID", example = "f5a8779a-bcc9-4fc5-b8a1-7b2a383054a9")
        UUID volunteerId,
        @Schema(description = "모집글 ID", example = "101")
        Long recruitBoardId,
        @Schema(description = "지원 상태", example = "WAITING", allowableValues = {"WAITING", "APPROVED", "REJECTED"})
        ApplyStatus status,
        @Schema(description = "봉사 참여 여부", example = "false")
        Boolean attended,
        @Schema(description = "리뷰 작성 여부", example = "false")
        Boolean isReviewed,
        @Schema(description = "지원서 생성 일시", example = "2024-11-01T12:00:00")
        LocalDateTime createdAt,
        @Schema(description = "지원서 수정 일시", example = "2024-11-01T12:30:00")
        LocalDateTime updatedAt
) {

    public static VolunteerApplyWithReviewStatusResponseDto of(VolunteerApply volunteerApply, boolean isReviewed) {
        return VolunteerApplyWithReviewStatusResponseDto.builder()
                .id(volunteerApply.getId())
                .volunteerId(volunteerApply.getVolunteerId())
                .recruitBoardId(volunteerApply.getRecruitBoardId())
                .status(volunteerApply.getStatus())
                .attended(volunteerApply.getAttended())
                .isReviewed(isReviewed)
                .createdAt(volunteerApply.getCreatedAt())
                .updatedAt(volunteerApply.getUpdatedAt())
                .build();
    }
}
