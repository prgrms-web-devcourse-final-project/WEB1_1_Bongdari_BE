package com.somemore.volunteerapply.dto.request;

import static com.somemore.volunteerapply.domain.ApplyStatus.WAITING;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.volunteerapply.domain.VolunteerApply;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;

@JsonNaming(SnakeCaseStrategy.class)
@Builder
public record VolunteerApplyCreateRequestDto(
        @Schema(description = "봉사 모집글 아이디", example = "1")
        @NotNull(message = "모집글 아이디는 필수 값입니다.")
        Long recruitBoardId
) {

    public VolunteerApply toEntity(UUID volunteerId) {
        return VolunteerApply.builder()
                .volunteerId(volunteerId)
                .recruitBoardId(recruitBoardId)
                .status(WAITING)
                .attended(false)
                .build();
    }

}
