package com.somemore.volunteerapply.controller;

import com.somemore.auth.annotation.CurrentUser;
import com.somemore.global.common.response.ApiResponse;
import com.somemore.volunteerapply.dto.VolunteerApplyCreateRequestDto;
import com.somemore.volunteerapply.usecase.ApplyVolunteerApplyUseCase;
import com.somemore.volunteerapply.usecase.WithdrawVolunteerApplyUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Volunteer Apply Command API", description = "봉사 활동 지원, 철회 관련 API")
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class VolunteerApplyCommandApiController {

    private final ApplyVolunteerApplyUseCase applyVolunteerApplyUseCase;
    private final WithdrawVolunteerApplyUseCase withdrawVolunteerApplyUseCase;

    @Secured("ROLE_VOLUNTEER")
    @Operation(summary = "봉사 활동 지원", description = "봉사 활동에 지원합니다.")
    @PostMapping("/volunteer-apply")
    public ApiResponse<Long> apply(
            @CurrentUser UUID volunteerId,
            @Valid @RequestBody VolunteerApplyCreateRequestDto requestDto
    ) {
        return ApiResponse.ok(
                201,
                applyVolunteerApplyUseCase.apply(requestDto, volunteerId),
                "봉사 활동 지원 성공"
        );
    }

    @Secured("ROLE_VOLUNTEER")
    @Operation(summary = "봉사 활동 지원 철회", description = "봉사 활동 지원을 철회합니다.")
    @DeleteMapping("/volunteer-apply/{id}")
    public ApiResponse<String> withdraw(
            @CurrentUser UUID volunteerId,
            @PathVariable Long id
    ) {
        withdrawVolunteerApplyUseCase.withdraw(id, volunteerId);
        return ApiResponse.ok("봉사 활동 지원 철회 성공");
    }

}
