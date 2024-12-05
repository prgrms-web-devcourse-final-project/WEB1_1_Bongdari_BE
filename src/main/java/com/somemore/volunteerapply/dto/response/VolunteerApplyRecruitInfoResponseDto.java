package com.somemore.volunteerapply.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.recruitboard.domain.RecruitBoard;
import com.somemore.recruitboard.dto.response.RecruitBoardOverViewResponseDto;
import com.somemore.volunteerapply.domain.ApplyStatus;
import com.somemore.volunteerapply.domain.VolunteerApply;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

@JsonNaming(SnakeCaseStrategy.class)
@Builder
public record VolunteerApplyRecruitInfoResponseDto(
        @Schema(description = "봉사 지원 ID", example = "123")
        Long id,
        @Schema(description = "봉사자 UUID", example = "f5a8779a-bcc9-4fc5-b8a1-7b2a383054a9")
        UUID volunteerId,
        @Schema(description = "지원 상태", example = "WAITING", allowableValues = {"WAITING",
                "APPROVED", "REJECTED"})
        ApplyStatus status,
        @Schema(description = "참석 여부", example = "true")
        Boolean attend,
        @Schema(description = "지원 생성일", example = "2024-11-01T12:00:00")
        LocalDateTime createdAt,
        @Schema(description = "지원 수정일", example = "2024-11-05T12:00:00")
        LocalDateTime updatedAt,
        @Schema(description = "지원한 모집글 정보", implementation = RecruitBoardOverViewResponseDto.class)
        RecruitBoardOverViewResponseDto recruitBoard
) {

    public static VolunteerApplyRecruitInfoResponseDto of(VolunteerApply apply,
            RecruitBoard board) {
        RecruitBoardOverViewResponseDto recruitBoard = RecruitBoardOverViewResponseDto.from(board);
        return VolunteerApplyRecruitInfoResponseDto.builder()
                .id(apply.getId())
                .volunteerId(apply.getVolunteerId())
                .status(apply.getStatus())
                .attend(apply.getAttended())
                .createdAt(apply.getCreatedAt())
                .updatedAt(apply.getUpdatedAt())
                .recruitBoard(recruitBoard)
                .build();
    }

}
