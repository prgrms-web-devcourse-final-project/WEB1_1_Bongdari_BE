package com.somemore.domains.community.controller;

import com.somemore.domains.community.dto.response.CommunityCommentResponseDto;
import com.somemore.domains.community.usecase.comment.CommunityCommentQueryUseCase;
import com.somemore.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Community Comment Query API", description = "커뮤니티 댓글 조회 API")
@RequiredArgsConstructor
@RequestMapping("/api/community-board")
@RestController
public class CommunityCommentQueryApiController {

    private final CommunityCommentQueryUseCase communityCommentQueryUseCase;

    @GetMapping("/{boardId}/comments")
    @Operation(summary = "커뮤니티 댓글 조회", description = "커뮤니티 게시글의 댓글 목록을 조회합니다.")
    public ApiResponse<Page<CommunityCommentResponseDto>> getByBoardId(
            @PathVariable Long boardId,
            Pageable pageable
    ) {
        return ApiResponse.ok(
                200,
                communityCommentQueryUseCase.getCommunityCommentsByBoardId(boardId, pageable.getPageNumber()),
                "커뮤니티 게시글의 댓글 리스트 조회 성공"
        );
    }
}
