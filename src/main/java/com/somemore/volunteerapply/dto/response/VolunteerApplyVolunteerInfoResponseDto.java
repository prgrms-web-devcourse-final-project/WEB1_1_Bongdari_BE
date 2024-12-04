package com.somemore.volunteerapply.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.volunteer.dto.response.VolunteerSimpleInfoResponseDto;
import com.somemore.volunteerapply.domain.ApplyStatus;
import com.somemore.volunteerapply.domain.VolunteerApply;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;

@Schema(description = "봉사 지원자 상세 정보 응답 DTO")
@JsonNaming(SnakeCaseStrategy.class)
@Builder
public record VolunteerApplyVolunteerInfoResponseDto(
        @Schema(description = "봉사 지원 ID", example = "123")
        Long id,
        @Schema(description = "모집글 ID", example = "2")
        Long recruitBoardId,
        @Schema(description = "지원 상태", example = "WAITING", allowableValues = {"WAITING",
                "APPROVED", "REJECTED"})
        ApplyStatus status,
        @Schema(description = "참석 여부", example = "true")
        Boolean attend,
        @Schema(description = "지원 생성일", example = "2024-11-01T12:00:00")
        LocalDateTime createdAt,
        @Schema(description = "지원 수정일", example = "2024-11-05T12:00:00")
        LocalDateTime updatedAt,
        @Schema(description = "봉사자 정보", implementation = VolunteerSimpleInfoResponseDto.class)
        VolunteerSimpleInfoResponseDto volunteer
) {

    public static VolunteerApplyVolunteerInfoResponseDto of(
            VolunteerApply volunteerApply,
            VolunteerSimpleInfoResponseDto volunteer
    ) {
        return VolunteerApplyVolunteerInfoResponseDto.builder()
                .id(volunteerApply.getId())
                .recruitBoardId(volunteerApply.getRecruitBoardId())
                .status(volunteerApply.getStatus())
                .attend(volunteerApply.getAttended())
                .createdAt(volunteerApply.getCreatedAt())
                .updatedAt(volunteerApply.getUpdatedAt())
                .volunteer(volunteer)
                .build();
    }
}
