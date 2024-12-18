package com.somemore.domains.volunteer.controller;

import com.somemore.domains.volunteer.dto.response.VolunteerRankingResponseDto;
import com.somemore.domains.volunteer.usecase.VolunteerQueryUseCase;
import com.somemore.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/volunteer/ranking")
@Tag(name = "GET Volunteer ranking", description = "봉사자 랭킹 조회")
public class VolunteerRankingQueryController {

    private final VolunteerQueryUseCase volunteerQueryUseCase;

    @Operation(summary = "봉사 시간 랭킹 조회", description = "봉사 시간 내림차순 4명 조회")
    @GetMapping("/hours")
    public ApiResponse<VolunteerRankingResponseDto> getRankingByHours() {

        return ApiResponse.ok(
                200,
                volunteerQueryUseCase.getRankingByHours(),
                "랭킹(시간) 조회 성공");
    }
}
