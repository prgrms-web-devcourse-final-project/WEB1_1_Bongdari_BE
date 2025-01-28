package com.somemore.user.controller;

import com.somemore.global.auth.annotation.UserId;
import com.somemore.global.common.response.ApiResponse;
import com.somemore.user.dto.basicinfo.CenterBasicInfoRequestDto;
import com.somemore.user.dto.basicinfo.VolunteerBasicInfoRequestDto;
import com.somemore.user.usecase.UpdateBasicInfoUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "User Command API", description = "유저 생성 수정 삭제 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserCommandController {

    private final UpdateBasicInfoUseCase updateBasicInfoUseCase;

    @Secured("ROLE_VOLUNTEER")
    @PutMapping("/basic-info/volunteer")
    @Operation(summary = "봉사자 기본 정보 업데이트", description = "봉사자의 기본 정보를 업데이트합니다.")
    public ApiResponse<String> registerBasicInfo(
            @UserId UUID userId,
            @RequestBody VolunteerBasicInfoRequestDto volunteerBasicInfoRequestDto
    ) {
        updateBasicInfoUseCase.update(userId, volunteerBasicInfoRequestDto);
        return ApiResponse.ok("봉사자 기본 정보 업데이트 완료");
    }

    @Secured("ROLE_CENTER")
    @PutMapping("/basic-info/center")
    @Operation(summary = "센터 기본 정보 업데이트", description = "센터의 기본 정보를 업데이트합니다.")
    public ApiResponse<String> registerBasicInfo(
            @UserId UUID userId,
            @RequestBody CenterBasicInfoRequestDto centerBasicInfoRequestDto
    ) {
        updateBasicInfoUseCase.update(userId, centerBasicInfoRequestDto);
        return ApiResponse.ok("센터 기본 정보 업데이 완료");
    }
}
