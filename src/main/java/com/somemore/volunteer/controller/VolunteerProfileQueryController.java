package com.somemore.volunteer.controller;

import com.somemore.global.auth.annotation.UserId;
import com.somemore.global.common.response.ApiResponse;
import com.somemore.volunteer.dto.VolunteerProfileResponseDto;
import com.somemore.volunteer.usecase.GetVolunteerProfileUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/volunteer/profile")
@Tag(name = "GET Volunteer Profile", description = "봉사자 조회")
public class VolunteerProfileQueryController {

    private final GetVolunteerProfileUseCase getVolunteerProfileUseCase;

    @Operation(summary = "본인 상세 프로필 조회", description = "현재 로그인된 사용자의 상세 프로필을 조회합니다.")
    @Secured("ROLE_VOLUNTEER")
    @GetMapping("/me")
    public ApiResponse<VolunteerProfileResponseDto> getMyProfile(
            @UserId UUID userId) {
        return ApiResponse.ok(
                200,
                getVolunteerProfileUseCase.getProfileByUserId(userId),
                "본인 프로필 조회 성공");
    }

    @GetMapping("/user-id/{userId}")
    @Operation(summary = "타인 프로필 조회 (유저 아이디)", description = "유저 아이디로 특정 봉사자의 상세 프로필을 조회합니다.")
    public ApiResponse<VolunteerProfileResponseDto> getVolunteerProfileByUserId(
            @PathVariable UUID userId) {
        return ApiResponse.ok(
                200,
                getVolunteerProfileUseCase.getProfileByUserId(userId),
                "타인 프로필 조회 성공"
        );
    }

    @GetMapping("/volunteer-id/{volunteerId}")
    @Operation(summary = "타인 프로필 조회 (봉사자 아아디)", description = "봉사자 아이디특정 봉사자의 상세 프로필을 조회합니다.")
    public ApiResponse<VolunteerProfileResponseDto> getVolunteerProfileByVolunteerId(
            @PathVariable UUID volunteerId) {
        return ApiResponse.ok(
                200,
                getVolunteerProfileUseCase.getProfileByVolunteerId(volunteerId),
                "타인 프로필 조회 성공"
        );
    }
}
