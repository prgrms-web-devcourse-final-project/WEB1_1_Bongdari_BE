package com.somemore.recruitboard.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.location.dto.request.LocationCreateRequestDto;
import com.somemore.recruitboard.domain.RecruitBoard;
import com.somemore.recruitboard.domain.VolunteerType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;
import lombok.Builder;

@JsonNaming(SnakeCaseStrategy.class)
@Builder
public record RecruitBoardCreateRequestDto(
    @Schema(description = "봉사 모집글 제목", example = "서울 청계천 환경 미화 봉사 모집")
    @NotBlank(message = "모집글 제목은 필수 값입니다.")
    String title,
    @Schema(description = "봉사 모집글 내용", example = "서울 청계천 주변 환경 미화 봉사 모집합니다. <br>")
    @NotBlank(message = "모집글 내용은 필수 값입니다.")
    String content,
    @Schema(description = "봉사 지역", example = "서울")
    @NotBlank(message = "봉사 지역은 필수 값입니다.")
    String region,
    @Schema(description = "예상 모집 인원", example = "4")
    @NotNull(message = "예상 모집 인원은 필수 값입니다.")
    Integer recruitmentCount,
    @Schema(description = "봉사 일시", example = "2024-11-20T10:00:00")
    @NotNull(message = "봉사 일시는 필수 값입니다.")
    LocalDateTime volunteerDate,
    @Schema(description = "봉사 활동 유형", example = "ENVIRONMENTAL_PROTECTION")
    @NotNull(message = "봉사 활동 유형은 필수 값입니다.")
    VolunteerType volunteerType,
    @Schema(description = "봉사 시간(시)", example = "1")
    @Positive(message = "봉사 시간(시)은 1이상 이어야 합니다.")
    Integer volunteerHours,
    @Schema(description = "봉사 시간(분)", example = "30")
    @Positive(message = "봉사 시간(분)은 1이상 이어야 합니다.")
    Integer volunteerMinutes,
    @Schema(description = "봉사 시간 인정 여부", example = "true")
    @NotNull(message = "시간 인정 여부는 필수 값입니다.")
    Boolean admitted,
    @NotNull(message = "위치 정보는 필수 값입니다.")
    LocationCreateRequestDto location
){
    public RecruitBoard toEntity(UUID centerId, Long locationId, String imgUrl) {
        return RecruitBoard.builder()
            .centerId(centerId)
            .locationId(locationId)
            .title(title)
            .content(content)
            .region(region)
            .recruitmentCount(recruitmentCount)
            .imgUrl(imgUrl)
            .volunteerDate(volunteerDate)
            .volunteerType(volunteerType)
            .volunteerHours(LocalTime.of(volunteerHours, volunteerMinutes))
            .admitted(admitted)
            .build();
    }
}
