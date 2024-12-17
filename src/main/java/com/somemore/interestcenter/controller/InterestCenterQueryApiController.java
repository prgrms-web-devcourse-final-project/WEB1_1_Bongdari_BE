package com.somemore.interestcenter.controller;

import com.somemore.global.auth.annotation.CurrentUser;
import com.somemore.global.common.response.ApiResponse;
import com.somemore.interestcenter.dto.response.InterestCentersResponseDto;
import com.somemore.interestcenter.usecase.InterestCenterQueryUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Tag(name = "Interest Center Query API", description = "관심 기관의 조회 API를 제공합니다")
public class InterestCenterQueryApiController {

    private final InterestCenterQueryUseCase interestCenterQueryUseCase;

    @Operation(summary = "관심기관 목록 조회 API")
    @GetMapping("/api/interest-centers")
    public ApiResponse<List<InterestCentersResponseDto>> getInterestCenters(
            @CurrentUser UUID volunteerId) {

        List<InterestCentersResponseDto> responseDtos = interestCenterQueryUseCase.getInterestCenters(
                volunteerId);

        return ApiResponse.ok(200, responseDtos, "관심기관 조회 성공");
    }

}
