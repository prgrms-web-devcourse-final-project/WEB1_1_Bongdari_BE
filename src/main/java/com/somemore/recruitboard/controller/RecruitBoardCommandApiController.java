package com.somemore.recruitboard.controller;


import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import com.somemore.global.common.response.ApiResponse;
import com.somemore.imageupload.dto.ImageUploadRequestDto;
import com.somemore.imageupload.usecase.ImageUploadUseCase;
import com.somemore.recruitboard.dto.request.RecruitBoardCreateRequestDto;
import com.somemore.recruitboard.dto.request.RecruitBoardLocationUpdateRequestDto;
import com.somemore.recruitboard.dto.request.RecruitBoardStatusUpdateRequestDto;
import com.somemore.recruitboard.dto.request.RecruitBoardUpdateRequestDto;
import com.somemore.recruitboard.usecase.command.CreateRecruitBoardUseCase;
import com.somemore.recruitboard.usecase.command.DeleteRecruitBoardUseCase;
import com.somemore.recruitboard.usecase.command.UpdateRecruitBoardUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "RecruitBoard", description = "봉사 활동 모집글 생성 수정 삭제 API")
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class RecruitBoardCommandApiController {

    private final CreateRecruitBoardUseCase createRecruitBoardUseCase;
    private final UpdateRecruitBoardUseCase updateRecruitBoardUseCase;
    private final DeleteRecruitBoardUseCase deleteRecruitBoardUseCase;
    private final ImageUploadUseCase imageUploadUseCase;

    @PreAuthorize("hasRole('CENTER')")
    @Operation(summary = "봉사 활동 모집글 등록", description = "봉사 활동 모집글을 등록합니다.")
    @PostMapping(value = "/recruit-board", consumes = MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Long> createRecruitBoard(
            @Valid @RequestPart("data") RecruitBoardCreateRequestDto requestDto,
            @RequestPart("img_file") MultipartFile image
    ) {

        String imgUrl = imageUploadUseCase.uploadImage(new ImageUploadRequestDto(image));
        return ApiResponse.ok(
                201,
                createRecruitBoardUseCase.createRecruitBoard(requestDto, getCenterId(), imgUrl),
                "봉사 활동 모집글 등록 성공"
        );
    }

    @PreAuthorize("hasRole('CENTER')")
    @Operation(summary = "봉사 활동 모집글 수정", description = "봉사 활동 모집글을 수정합니다.")
    @PutMapping(value = "/recruit-board/{id}", consumes = MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> updateRecruitBoard(
            @AuthenticationPrincipal String userId,
            @PathVariable Long id,
            @Valid @RequestPart("data") RecruitBoardUpdateRequestDto requestDto,
            @RequestPart("img_file") MultipartFile image
    ) {
        String imgUrl = imageUploadUseCase.uploadImage(new ImageUploadRequestDto(image));
        updateRecruitBoardUseCase.updateRecruitBoard(requestDto, id, getCenterId(), imgUrl);

        return ApiResponse.ok("봉사 활동 모집글 수정 성공");
    }

    @PreAuthorize("hasRole('CENTER')")
    @Operation(summary = "봉사 활동 모집글 위치 수정", description = "봉사 활동 모집글의 위치를 수정합니다.")
    @PutMapping(value = "/recruit-board/{id}/location")
    public ApiResponse<String> updateRecruitBoardLocation(
            @AuthenticationPrincipal String userId,
            @PathVariable Long id,
            @Valid @RequestBody RecruitBoardLocationUpdateRequestDto requestDto
    ) {

        updateRecruitBoardUseCase.updateRecruitBoardLocation(requestDto, id, getCenterId());
        return ApiResponse.ok("봉사 활동 모집글 위치 수정 성공");
    }

    @PreAuthorize("hasRole('CENTER')")
    @Operation(summary = "봉사 활동 모집글 상태 수정", description = "봉사 활동 모집글의 상태를 수정합니다.")
    @PatchMapping(value = "/recruit-board/{id}")
    public ApiResponse<String> updateRecruitBoardStatus(
            @AuthenticationPrincipal String userId,
            @PathVariable Long id,
            @RequestBody RecruitBoardStatusUpdateRequestDto requestDto
    ) {
        LocalDateTime now = LocalDateTime.now();
        updateRecruitBoardUseCase.updateRecruitBoardStatus(requestDto.status(), id, getCenterId(),
                now);

        return ApiResponse.ok("봉사 활동 모집글 상태 수정 성공");
    }

    @PreAuthorize("hasRole('CENTER')")
    @Operation(summary = "봉사 활동 모집글 삭제", description = "봉사 활동 모집글을 삭제합니다.")
    @DeleteMapping(value = "/recruit-board/{id}")
    public ApiResponse<String> deleteRecruitBoard(
            @AuthenticationPrincipal String userId,
            @PathVariable Long id
    ) {
        deleteRecruitBoardUseCase.deleteRecruitBoard(getCenterId(), id);
        return ApiResponse.ok("봉사 활동 모집글 삭제 성공");
    }

    private static UUID getCenterId() {
        return UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
    }

}
