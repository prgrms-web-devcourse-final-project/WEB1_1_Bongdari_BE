package com.somemore.volunteerapply.controller;

import com.somemore.global.common.response.ApiResponse;
import com.somemore.volunteerapply.dto.response.VolunteerApplySummaryResponseDto;
import com.somemore.volunteerapply.usecase.VolunteerApplyQueryUseCase;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class VolunteerApplyQueryApiController {

    private final VolunteerApplyQueryUseCase volunteerApplyQueryUseCase;

    @Operation(summary = "지원자 통계 조회", description = "특정 모집글에 대한 지원자 통계를 조회합니다.")
    @GetMapping("/volunteer-apply/recruit-board/{recruitBoardId}/summary")
    public ApiResponse<VolunteerApplySummaryResponseDto> getSummaryByRecruitBoardId(
            @PathVariable Long recruitBoardId
    ) {
        return ApiResponse.ok(
                200,
                volunteerApplyQueryUseCase.getSummaryByRecruitBoardId(recruitBoardId),
                "지원자 통계 조회 성공"
        );
    }
}
