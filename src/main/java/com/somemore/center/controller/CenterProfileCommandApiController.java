package com.somemore.center.controller;

import com.somemore.auth.annotation.CurrentUser;
import com.somemore.center.dto.request.CenterProfileUpdateRequestDto;
import com.somemore.center.usecase.command.UpdateCenterProfileUseCase;
import com.somemore.global.common.response.ApiResponse;
import com.somemore.imageupload.dto.ImageUploadRequestDto;
import com.somemore.imageupload.usecase.ImageUploadUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@Tag(name = "Center Command API", description = "센터 프로필 수정 API")
@RequiredArgsConstructor
@RequestMapping("/api/center/profile")
@RestController
public class CenterProfileCommandApiController {

    private final UpdateCenterProfileUseCase updateCenterProfileUseCase;
    private final ImageUploadUseCase imageUploadUseCase;

    @Secured("ROLE_CENTER")
    @Operation(summary = "센터 프로필 수정", description = "센터 프로필을 수정합니다.")
    @PutMapping(value = "/{centerId}", consumes = MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> updateCenterProfile(
            @CurrentUser UUID centerId,
            @Valid @RequestPart("data") CenterProfileUpdateRequestDto requestDto,
            @RequestPart(value = "img_file", required = false) MultipartFile image
    ) {
        String imgUrl = imageUploadUseCase.uploadImage(new ImageUploadRequestDto(image));
        updateCenterProfileUseCase.updateCenterProfile(centerId, requestDto, imgUrl);

        return ApiResponse.ok("센터 프로필 수정 성공");
    }
}
