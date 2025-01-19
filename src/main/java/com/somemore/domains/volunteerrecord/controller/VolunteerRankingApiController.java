package com.somemore.domains.volunteerrecord.controller;

import com.somemore.domains.volunteerrecord.dto.response.VolunteerRankingResponseDto;
import com.somemore.domains.volunteerrecord.usecase.GetVolunteerRankingUseCase;
import com.somemore.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Volunteer Ranking API", description = "봉사 시간 랭킹 API")
@RequiredArgsConstructor
@RequestMapping("/api/volunteerrecord")
@RestController
public class VolunteerRankingApiController {

    private final GetVolunteerRankingUseCase getVolunteerRankingUseCase;

    @Operation(summary = "봉사 시간 랭킹", description = "봉사 시간 랭킹을 반환합니다.")
    @GetMapping("/ranking")
    public ApiResponse<VolunteerRankingResponseDto> getVolunteerRanking() {

        VolunteerRankingResponseDto volunteerRankings = getVolunteerRankingUseCase.getVolunteerRanking();

        return ApiResponse.ok(volunteerRankings,"봉사 시간 랭킹 반환 성공");
    }
}
