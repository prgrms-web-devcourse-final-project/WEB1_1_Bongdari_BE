package com.somemore.domains.interestcenter.controller;

import com.somemore.domains.interestcenter.usecase.CancelInterestCenterUseCase;
import com.somemore.global.auth.annotation.CurrentUser;
import com.somemore.global.common.response.ApiResponse;
import com.somemore.domains.interestcenter.dto.request.RegisterInterestCenterRequestDto;
import com.somemore.domains.interestcenter.dto.response.RegisterInterestCenterResponseDto;
import com.somemore.domains.interestcenter.usecase.RegisterInterestCenterUseCase;
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
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Tag(name = "Interest Center Command API", description = "관심 기관의 등록과 취소 API를 제공합니다")
public class InterestCenterCommandApiController {

    private final RegisterInterestCenterUseCase registerInterestCenterUseCase;
    private final CancelInterestCenterUseCase cancelInterestCenterUseCase;

    @Secured("ROLE_VOLUNTEER")
    @Operation(summary = "관심기관 등록 API")
    @PostMapping("/api/interest-center")
    public ApiResponse<RegisterInterestCenterResponseDto> registerInterestCenter(
            @CurrentUser UUID volunteerId,
            @Valid @RequestBody RegisterInterestCenterRequestDto requestDto) {

        RegisterInterestCenterResponseDto responseDto = registerInterestCenterUseCase.registerInterestCenter(volunteerId, requestDto);

        return ApiResponse.ok(200, responseDto, "관심 기관 등록 성공");
    }

    @Secured("ROLE_VOLUNTEER")
    @Operation(summary = "관심기관 취소 API")
    @DeleteMapping("/api/interest-center/{centerId}")
    public ApiResponse<String> deleteInterestCenter(
            @CurrentUser UUID volunteerId,
            @PathVariable UUID centerId) {

        cancelInterestCenterUseCase.cancelInterestCenter(volunteerId, centerId);

        return ApiResponse.ok("관심 기관 취소 성공");
    }

}
