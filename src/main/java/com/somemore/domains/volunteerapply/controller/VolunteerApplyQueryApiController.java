package com.somemore.domains.volunteerapply.controller;

import com.somemore.domains.volunteerapply.domain.ApplyStatus;
import com.somemore.domains.volunteerapply.dto.condition.VolunteerApplySearchCondition;
import com.somemore.domains.volunteerapply.dto.response.VolunteerApplyRecruitInfoResponseDto;
import com.somemore.domains.volunteerapply.dto.response.VolunteerApplySummaryResponseDto;
import com.somemore.domains.volunteerapply.dto.response.VolunteerApplyVolunteerInfoResponseDto;
import com.somemore.domains.volunteerapply.dto.response.VolunteerApplyWithReviewStatusResponseDto;
import com.somemore.domains.volunteerapply.usecase.VolunteerApplyQueryFacadeUseCase;
import com.somemore.domains.volunteerapply.usecase.VolunteerApplyQueryUseCase;
import com.somemore.global.auth.annotation.CurrentUser;
import com.somemore.global.auth.annotation.RoleId;
import com.somemore.global.common.response.ApiResponse;
import com.somemore.global.exception.NoSuchElementException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Tag(name = "Volunteer Apply Query API", description = "봉사 활동 지원 조회 API")
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class VolunteerApplyQueryApiController {

    private final VolunteerApplyQueryUseCase volunteerApplyQueryUseCase;
    private final VolunteerApplyQueryFacadeUseCase volunteerApplyQueryFacadeUseCase;

    @Secured("ROLE_VOLUNTEER")
    @Operation(summary = "특정 모집글 봉사 지원 단건 조회", description = "특정 모집글에 대한 봉사 지원을 조회합니다.")
    @GetMapping("/volunteer-apply/recruit-board/{recruitBoardId}")
    public ApiResponse<VolunteerApplyWithReviewStatusResponseDto> getVolunteerApplyByRecruitBoardId(
            @PathVariable Long recruitBoardId,
            @RoleId UUID volunteerId
    ) {
        try {
            return ApiResponse.ok(
                    200,
                    volunteerApplyQueryFacadeUseCase.getVolunteerApplyByRecruitIdAndVolunteerId(recruitBoardId, volunteerId),
                    "특정 모집글에 대한 봉사 지원 단건 조회 성공"
            );
        } catch (NoSuchElementException e) {
            return ApiResponse.ok(210, null, "지원 내역이 없습니다.");
        }
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

    @Operation(summary = "특정 봉사자 봉사 지원 리스트 조회", description = "특정 봉사자의 봉사 지원 리스트를 조회합니다.")
    @GetMapping("/volunteer-applies/volunteer/{volunteerId}")
    public ApiResponse<Page<VolunteerApplyRecruitInfoResponseDto>> getVolunteerAppliesByVolunteerId(
            @PathVariable UUID volunteerId,
            @PageableDefault(sort = "created_at", direction = DESC) Pageable pageable,
            @RequestParam(required = false) Boolean attended,
            @RequestParam(required = false) ApplyStatus status
    ) {
        VolunteerApplySearchCondition condition = VolunteerApplySearchCondition.builder()
                .attended(attended)
                .status(status)
                .pageable(pageable)
                .build();

        return ApiResponse.ok(
                200,
                volunteerApplyQueryFacadeUseCase.getVolunteerAppliesByVolunteerId(volunteerId,
                        condition),
                "봉사 지원 리스트 조회 성공"
        );
    }

    @Secured("ROLE_CENTER")
    @Operation(summary = "지원자 리스트 조회", description = "특정 모집글에 대한 지원자 리스트를 조회합니다.")
    @GetMapping("/volunteer-applies/recruit-board/{recruitBoardId}")
    public ApiResponse<Page<VolunteerApplyVolunteerInfoResponseDto>> getVolunteerApplies(
            @RoleId UUID centerId,
            @PathVariable Long recruitBoardId,
            @PageableDefault(sort = "created_at", direction = DESC) Pageable pageable,
            @RequestParam(required = false) Boolean attended,
            @RequestParam(required = false) ApplyStatus status
    ) {
        VolunteerApplySearchCondition condition = VolunteerApplySearchCondition.builder()
                .attended(attended)
                .status(status)
                .pageable(pageable)
                .build();
        return ApiResponse.ok(
                200,
                volunteerApplyQueryFacadeUseCase.getVolunteerAppliesByRecruitIdAndCenterId(
                        recruitBoardId, centerId, condition),
                "지원자 리스트 조회 성공"
        );
    }
}
