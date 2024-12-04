package com.somemore.volunteerapply.controller;

import com.somemore.global.common.response.ApiResponse;
import com.somemore.volunteerapply.dto.response.VolunteerApplyResponseDto;
import com.somemore.volunteerapply.dto.response.VolunteerApplySummaryResponseDto;
import com.somemore.volunteerapply.usecase.VolunteerApplyQueryUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Volunteer Apply Query API", description = "봉사 활동 지원 조회 API")
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class VolunteerApplyQueryApiController {

    private final VolunteerApplyQueryUseCase volunteerApplyQueryUseCase;

    @Operation(summary = "특정 모집글 봉사자 지원 단건 조회", description = "특정 모집글에 대한 봉사자 지원을 조회합니다.")
    @GetMapping("/volunteer-apply/recruit-board/{recruitBoardId}/volunteer/{volunteerId}")
    public ApiResponse<VolunteerApplyResponseDto> getVolunteerApplyByRecruitIdAndVolunteerId(
            @PathVariable Long recruitBoardId,
            @PathVariable UUID volunteerId
    ) {
        return ApiResponse.ok(
                200,
                volunteerApplyQueryUseCase.getVolunteerApplyByRecruitIdAndVolunteerId(
                        recruitBoardId, volunteerId),
                "특정 모집글에 대한 봉사자 지원 단건 조회 성공"
        );
    }

    @Operation(summary = "지원자 통계 조회", description = "특정 모집글에 대한 지원자 통계를 조회합니다.")
    @GetMapping("/volunteer-apply/recruit-board/{recruitBoardId}/summary")
    public ApiResponse<VolunteerApplySummaryResponseDto> getSummaryByRecruitId(
            @PathVariable Long recruitBoardId
    ) {
        return ApiResponse.ok(
                200,
                volunteerApplyQueryUseCase.getSummaryByRecruitId(recruitBoardId),
                "지원자 통계 조회 성공"
        );
    }
}
