package com.somemore.community.controller;

import com.somemore.auth.annotation.CurrentUser;
import com.somemore.community.dto.request.CommunityCommentCreateRequestDto;
import com.somemore.community.dto.request.CommunityCommentUpdateRequestDto;
import com.somemore.community.usecase.comment.CreateCommunityCommentUseCase;
import com.somemore.community.usecase.comment.DeleteCommunityCommentUseCase;
import com.somemore.community.usecase.comment.UpdateCommunityCommentUseCase;
import com.somemore.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Community Comment Command API", description = "커뮤니티 댓글 생성 수정 삭제 API")
@RequiredArgsConstructor
@RequestMapping("/api/community-board/{boardId}")
@RestController
public class CommunityCommentCommandApiController {

    private final CreateCommunityCommentUseCase createCommunityCommentUseCase;
    private final UpdateCommunityCommentUseCase updateCommunityCommentUseCase;
    private final DeleteCommunityCommentUseCase deleteCommunityCommentUseCase;

    @Secured("ROLE_VOLUNTEER")
    @Operation(summary = "커뮤니티 댓글 등록", description = "커뮤니티 게시글에 댓글을 등록합니다.")
    @PostMapping(value = "/comment")
    public ApiResponse<Long> createCommunityComment(
            @CurrentUser UUID userId,
            @PathVariable Long boardId,
            @Valid @RequestBody CommunityCommentCreateRequestDto requestDto) {

        return ApiResponse.ok(
                201,
                createCommunityCommentUseCase.createCommunityComment(requestDto, userId, boardId),
                "커뮤니티 댓글 등록 성공");
    }

    @Secured("ROLE_VOLUNTEER")
    @Operation(summary = "커뮤니티 댓글 수정", description = "커뮤니티 댓글을 수정합니다.")
    @PutMapping(value = "/comment/{id}")
    public ApiResponse<String> updateCommunityComment(
            @CurrentUser UUID userId,
            @PathVariable Long boardId,
            @PathVariable Long id,
            @Valid @RequestBody CommunityCommentUpdateRequestDto requestDto
    ) {
        updateCommunityCommentUseCase.updateCommunityComment(requestDto, id, userId, boardId);

        return ApiResponse.ok("커뮤니티 댓글 수정 성공");
    }

    @Secured("ROLE_VOLUNTEER")
    @Operation(summary = "커뮤니티 댓글 삭제", description = "커뮤니티 댓글을 삭제합니다.")
    @DeleteMapping(value = "/comment/{id}")
    public ApiResponse<String> deleteCommunityComment(
            @CurrentUser UUID userId,
            @PathVariable Long boardId,
            @PathVariable Long id
    ) {
        deleteCommunityCommentUseCase.deleteCommunityComment(userId, id, boardId);

        return ApiResponse.ok("커뮤니티 댓글 삭제 성공");
    }
}
