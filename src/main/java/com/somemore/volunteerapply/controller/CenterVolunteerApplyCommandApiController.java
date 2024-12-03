package com.somemore.volunteerapply.controller;

import com.somemore.auth.annotation.CurrentUser;
import com.somemore.global.common.response.ApiResponse;
import com.somemore.volunteerapply.usecase.ApproveVolunteerApplyUseCase;
import com.somemore.volunteerapply.usecase.RejectVolunteerApplyUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Center Volunteer Apply Command API", description = "봉사 활동 지원 승인, 거절, 정산 API")
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class CenterVolunteerApplyCommandApiController {

    private final ApproveVolunteerApplyUseCase approveVolunteerApplyUseCase;
    private final RejectVolunteerApplyUseCase rejectVolunteerApplyUseCase;

    @Secured("ROLE_CENTER")
    @Operation(summary = "봉사 활동 지원 승인", description = "봉사 활동 지원을 승인합니다.")
    @PatchMapping("/volunteer-apply/{id}/approve")
    public ApiResponse<String> approve(
            @CurrentUser UUID centerId,
            @PathVariable Long id
    ) {

        approveVolunteerApplyUseCase.approve(id, centerId);
        return ApiResponse.ok("봉사 활동 지원 승인 성공");
    }

    @Secured("ROLE_CENTER")
    @Operation(summary = "봉사 활동 지원 거절", description = "봉사 활동 지원을 거절합니다.")
    @PatchMapping("/volunteer-apply/{id}/reject")
    public ApiResponse<String> reject(
            @CurrentUser UUID centerId,
            @PathVariable Long id
    ) {

        rejectVolunteerApplyUseCase.reject(id, centerId);
        return ApiResponse.ok("봉사 활동 지원 거절 성공");
    }

}
