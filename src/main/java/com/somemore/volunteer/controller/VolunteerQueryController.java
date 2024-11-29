package com.somemore.volunteer.controller;

import com.somemore.global.common.response.ApiResponse;
import com.somemore.volunteer.dto.response.VolunteerResponseDto;
import com.somemore.volunteer.usecase.VolunteerQueryUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Find Volunteer", description = "Find Volunteer")
public class VolunteerQueryController {

    private final VolunteerQueryUseCase volunteerQueryUseCase;

    @GetMapping("/profile/me")
    @Operation(summary = "본인 상세 프로필 조회", description = "현재 로그인된 사용자의 상세 프로필을 조회합니다.")
    public ApiResponse<VolunteerResponseDto> getMyProfile(
            @AuthenticationPrincipal String volunteerId) {

        return ApiResponse.ok(
                200,
                volunteerQueryUseCase.getMyProfile(UUID.fromString(volunteerId)),
                "프로필 조회 성공");
    }

    @GetMapping("/profile/{volunteerId}")
    @Operation(summary = "타인 프로필 조회", description = "특정 봉사자의 프로필을 조회합니다. 상세 정보는 포함되지 않습니다.")
    public ApiResponse<VolunteerResponseDto> getVolunteerProfile(
            @PathVariable UUID volunteerId) {

        return ApiResponse.ok(
                200,
                volunteerQueryUseCase.getVolunteerProfile(volunteerId),
                "프로필 조회 성공"
        );
    }

    @GetMapping("/profile/{volunteerId}/detailed")
    @Operation(summary = "타인 상세 프로필 조회", description = "기관이 작성한 모집 글에 지원한 봉사자의 상세 프로필을 조회합니다.")
    public ApiResponse<VolunteerResponseDto> getVolunteerDetailedProfile(
            @PathVariable UUID volunteerId,
            @AuthenticationPrincipal String centerId) {

        return ApiResponse.ok(
                200,
                volunteerQueryUseCase.getVolunteerDetailedProfile(volunteerId, UUID.fromString(centerId)),
                "프로필 조회 성공"
        );
    }
}