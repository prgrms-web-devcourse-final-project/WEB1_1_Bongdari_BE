package com.somemore.domains.recruitboard.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.domains.recruitboard.domain.RecruitBoard;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@JsonNaming(SnakeCaseStrategy.class)
@Builder
public record RecruitBoardOverViewResponseDto(
        @Schema(description = "모집글 아이디", example = "1")
        Long id,
        @Schema(description = "모집글 제목", example = "서울시 도서관 봉사 활동 모집")
        String title
) {

    public static RecruitBoardOverViewResponseDto from(RecruitBoard recruitBoard) {
        return RecruitBoardOverViewResponseDto.builder()
                .id(recruitBoard.getId())
                .title(recruitBoard.getTitle())
                .build();
    }
}
