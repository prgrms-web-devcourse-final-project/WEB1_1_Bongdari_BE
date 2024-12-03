package com.somemore.community.controller;

import com.somemore.auth.annotation.CurrentUser;
import com.somemore.community.dto.request.CommunityBoardCreateRequestDto;
import com.somemore.community.dto.request.CommunityBoardUpdateRequestDto;
import com.somemore.community.usecase.board.CreateCommunityBoardUseCase;
import com.somemore.community.usecase.board.DeleteCommunityBoardUseCase;
import com.somemore.community.usecase.board.UpdateCommunityBoardUseCase;
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

@Tag(name = "Community Board Command API", description = "커뮤니티 게시글 생성 수정 삭제 API")
@RequiredArgsConstructor
@RequestMapping("/api/community-board")
@RestController
public class CommunityBoardCommandApiController {

    private final CreateCommunityBoardUseCase createCommunityBoardUseCase;
    private final UpdateCommunityBoardUseCase updateCommunityBoardUseCase;
    private final DeleteCommunityBoardUseCase deleteCommunityBoardUseCase;
    private final ImageUploadUseCase imageUploadUseCase;

    @Secured("ROLE_VOLUNTEER")
    @Operation(summary = "커뮤니티 게시글 등록", description = "커뮤니티 게시글을 등록합니다.")
    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Long> createCommunityBoard(
            @CurrentUser UUID user_id,
            @Valid @RequestPart("data") CommunityBoardCreateRequestDto requestDto,
            @RequestPart(value = "img_file", required = false) MultipartFile image
    ) {
        String imgUrl = imageUploadUseCase.uploadImage(new ImageUploadRequestDto(image));

        return ApiResponse.ok(
                201,
                createCommunityBoardUseCase.createCommunityBoard(requestDto, user_id, imgUrl),
                "커뮤니티 게시글 등록 성공"
        );
    }

    @Secured("ROLE_VOLUNTEER")
    @Operation(summary = "커뮤니티 게시글 수정", description = "커뮤니티 게시글을 수정합니다.")
    @PutMapping(value = "/{id}", consumes = MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> updateCommunityBoard(
            @CurrentUser UUID user_id,
            @PathVariable Long id,
            @Valid @RequestPart("data") CommunityBoardUpdateRequestDto requestDto,
            @RequestPart(value = "img_file", required = false) MultipartFile image
    ) {
        String imgUrl = imageUploadUseCase.uploadImage(new ImageUploadRequestDto(image));
        updateCommunityBoardUseCase.updateCommunityBoard(requestDto, id, user_id, imgUrl);

        return ApiResponse.ok("커뮤니티 게시글 수정 성공");
    }

    @Secured("ROLE_VOLUNTEER")
    @Operation(summary = "커뮤니티 게시글 삭제", description = "커뮤니티 게시글을 삭제합니다.")
    @DeleteMapping(value = "/{id}")
    public ApiResponse<String> deleteCommunityBoard(
            @CurrentUser UUID user_id,
            @PathVariable Long id
    ) {
        deleteCommunityBoardUseCase.deleteCommunityBoard(user_id, id);

        return ApiResponse.ok("커뮤니티 게시글 삭제 성공");
    }
}
