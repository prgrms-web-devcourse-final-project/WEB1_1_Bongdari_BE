package com.somemore.domains.community.controller;

import com.somemore.domains.community.dto.request.CommunityBoardCreateRequestDto;
import com.somemore.domains.community.dto.request.CommunityBoardUpdateRequestDto;
import com.somemore.domains.community.usecase.board.CreateCommunityBoardUseCase;
import com.somemore.domains.community.usecase.board.DeleteCommunityBoardUseCase;
import com.somemore.domains.community.usecase.board.UpdateCommunityBoardUseCase;
import com.somemore.global.auth.annotation.RoleId;
import com.somemore.global.common.response.ApiResponse;
import com.somemore.global.imageupload.dto.ImageUploadRequestDto;
import com.somemore.global.imageupload.usecase.ImageUploadUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
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
            @RoleId UUID volunteerId,
            @Valid @RequestPart("data") CommunityBoardCreateRequestDto requestDto,
            @RequestPart(value = "img_file", required = false) MultipartFile image
    ) {
        String imgUrl = imageUploadUseCase.uploadImage(new ImageUploadRequestDto(image));

        return ApiResponse.ok(
                201,
                createCommunityBoardUseCase.createCommunityBoard(requestDto, volunteerId, imgUrl),
                "커뮤니티 게시글 등록 성공"
        );
    }

    @Secured("ROLE_VOLUNTEER")
    @Operation(summary = "커뮤니티 게시글 수정", description = "커뮤니티 게시글을 수정합니다.")
    @PutMapping(value = "/{id}", consumes = MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> updateCommunityBoard(
            @RoleId UUID volunteerId,
            @PathVariable Long id,
            @Valid @RequestPart("data") CommunityBoardUpdateRequestDto requestDto,
            @RequestPart(value = "img_file", required = false) MultipartFile image
    ) {
        String imgUrl = imageUploadUseCase.uploadImage(new ImageUploadRequestDto(image));
        updateCommunityBoardUseCase.updateCommunityBoard(requestDto, id, volunteerId, imgUrl);

        return ApiResponse.ok("커뮤니티 게시글 수정 성공");
    }

    @Secured("ROLE_VOLUNTEER")
    @Operation(summary = "커뮤니티 게시글 삭제", description = "커뮤니티 게시글을 삭제합니다.")
    @DeleteMapping(value = "/{id}")
    public ApiResponse<String> deleteCommunityBoard(
            @RoleId UUID volunteerId,
            @PathVariable Long id
    ) {
        deleteCommunityBoardUseCase.deleteCommunityBoard(volunteerId, id);

        return ApiResponse.ok("커뮤니티 게시글 삭제 성공");
    }
}
