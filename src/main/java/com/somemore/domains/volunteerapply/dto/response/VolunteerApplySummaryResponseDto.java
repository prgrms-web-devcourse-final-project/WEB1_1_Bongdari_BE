package com.somemore.domains.volunteerapply.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.domains.volunteerapply.domain.ApplyStatus;
import com.somemore.domains.volunteerapply.domain.VolunteerApply;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.somemore.domains.volunteerapply.domain.ApplyStatus.APPROVED;
import static com.somemore.domains.volunteerapply.domain.ApplyStatus.REJECTED;
import static com.somemore.domains.volunteerapply.domain.ApplyStatus.WAITING;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public record VolunteerApplySummaryResponseDto(
        @Schema(description = "대기 인원", example = "5")
        Long waiting,
        @Schema(description = "승인 인원", example = "10")
        Long approve,
        @Schema(description = "거절 인원", example = "2")
        Long reject,
        @Schema(description = "총 지원자 수", example = "17")
        Long total
) {

    public static VolunteerApplySummaryResponseDto from(List<VolunteerApply> volunteerApplies) {
        Map<ApplyStatus, Long> statusCountMap = volunteerApplies.stream()
                .collect(Collectors.groupingBy(VolunteerApply::getStatus, Collectors.counting()));

        long waitingCount = statusCountMap.getOrDefault(WAITING, 0L);
        long approveCount = statusCountMap.getOrDefault(APPROVED, 0L);
        long rejectCount = statusCountMap.getOrDefault(REJECTED, 0L);
        long totalCount = waitingCount + approveCount + rejectCount;

        return VolunteerApplySummaryResponseDto.builder()
                .waiting(waitingCount)
                .approve(approveCount)
                .reject(rejectCount)
                .total(totalCount)
                .build();
    }
}
