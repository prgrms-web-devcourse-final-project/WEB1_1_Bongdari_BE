package com.somemore.center.controller;

import com.somemore.center.dto.request.CenterProfileImgUpdateRequestDto;
import com.somemore.center.usecase.UpdateCenterProfileImgUseCase;
import com.somemore.global.auth.annotation.UserId;
import com.somemore.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Center Command API", description = "기관 프로필 수정 API")
@RequiredArgsConstructor
@RequestMapping("/api/center")
@RestController
public class CenterCommandApiController {

    private final UpdateCenterProfileImgUseCase updateCenterProfileImgUseCase;

    @Secured("ROLE_CENTER")
    @Operation(summary = "기관 프로필 이미지 수정", description = "기관의 프로필 이미지를 수정합니다.")
    @PutMapping("/profileImgUpdate")
    public ApiResponse<String> updateCenterProfile(
            @UserId UUID centerId,
            CenterProfileImgUpdateRequestDto requestDto
    ) {

        String presignedUrl = updateCenterProfileImgUseCase.updateCenterProfileImg(centerId, requestDto);

        return ApiResponse.ok(presignedUrl,"센터 프로필 수정 성공");
    }
}
