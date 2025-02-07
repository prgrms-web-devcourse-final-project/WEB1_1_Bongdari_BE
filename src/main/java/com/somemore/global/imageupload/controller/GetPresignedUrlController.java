package com.somemore.global.imageupload.controller;

import com.somemore.global.auth.annotation.UserId;
import com.somemore.global.common.response.ApiResponse;
import com.somemore.global.imageupload.usecase.ImageUploadUseCase;
import com.somemore.user.dto.request.ImgUrlRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Presigned URL Query API", description = "이미지 업로드 URL 조회 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/image")
public class GetPresignedUrlController {

    private final ImageUploadUseCase imageUploadUseCase;

    @Secured("ROLE_VOLUNTEER, ROLE_CENTER")
    @PostMapping("/upload")
    @Operation(summary = "이미지 업로드 URL 조회", description = "이미지 업로드 URL을 조회합니다.")
    public ApiResponse<String> getImageUploadUrl(
            @UserId UUID userId,
            @RequestBody ImgUrlRequestDto dto
    ) {
        return ApiResponse.ok(imageUploadUseCase.getPresignedUrl(dto.fileName()).value(),
                "이미지 업로드 URL 발급 성공");
    }
}
