package com.somemore.user.controller;

import com.somemore.global.auth.annotation.UserId;
import com.somemore.global.common.response.ApiResponse;
import com.somemore.user.dto.basicinfo.CenterBasicInfoRequestDto;
import com.somemore.user.dto.basicinfo.VolunteerBasicInfoRequestDto;
import com.somemore.user.dto.request.ImgUrlRequestDto;
import com.somemore.user.usecase.UpdateBasicInfoUseCase;
import com.somemore.user.usecase.UpdateProfileImgUrlUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
    private final UpdateProfileImgUrlUseCase updateProfileImgUrlUseCase;

    @Secured("ROLE_VOLUNTEER")
    @PutMapping("/basic-info/volunteer")
    @Operation(summary = "봉사자 기본 정보 수정", description = "봉사자의 기본 정보를 수정합니다.")
    public ApiResponse<String> updateBasicInfo(
            @UserId UUID userId,
            @Valid @RequestBody VolunteerBasicInfoRequestDto volunteerBasicInfoRequestDto
    ) {
        updateBasicInfoUseCase.update(userId, volunteerBasicInfoRequestDto);
        return ApiResponse.ok("봉사자 기본 정보 수정 완료");
    }

    @Secured("ROLE_CENTER")
    @PutMapping("/basic-info/center")
    @Operation(summary = "센터 기본 정보 수정", description = "센터의 기본 정보를 수정합니다.")
    public ApiResponse<String> updateBasicInfo(
            @UserId UUID userId,
            @Valid @RequestBody CenterBasicInfoRequestDto centerBasicInfoRequestDto
    ) {
        updateBasicInfoUseCase.update(userId, centerBasicInfoRequestDto);
        return ApiResponse.ok("센터 기본 정보 수정 완료");
    }

    @Secured({"ROLE_VOLUNTEER", "ROLE_CENTER"})
    @PutMapping("/image/volunteer")
    @Operation(summary = "유저 프로필 이미지 수정", description = "프로필 이미지를 수정합니다. 응답으로 제공되는 URL에 이미지를 PUT 해야 합니다.")
    public ApiResponse<String> updateVolunteerImage(
            @UserId UUID userId,
            @Valid @RequestBody ImgUrlRequestDto imgUrlRequestDto
    ) {
        updateProfileImgUrlUseCase.update(userId, imgUrlRequestDto);
        return ApiResponse.ok("프로필 이미지 수정 완료");
    }
}
