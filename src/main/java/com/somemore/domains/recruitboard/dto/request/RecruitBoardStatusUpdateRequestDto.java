package com.somemore.domains.recruitboard.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.domains.recruitboard.domain.RecruitStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@JsonNaming(SnakeCaseStrategy.class)
@Schema(description = "봉사 활동 모집글 상태 수정 요청 DTO")
public record RecruitBoardStatusUpdateRequestDto(
        @Schema(description = "변경할 봉사 활동 모집글의 상태", example = "CLOSED")
        RecruitStatus status
) {

}
