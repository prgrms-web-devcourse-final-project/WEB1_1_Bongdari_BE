package com.somemore.community.controller;

import com.somemore.auth.annotation.CurrentUser;
import com.somemore.community.dto.response.CommunityBoardDetailResponseDto;
import com.somemore.community.dto.response.CommunityBoardResponseDto;
import com.somemore.community.usecase.board.CommunityBoardQueryUseCase;
import com.somemore.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Community Board Query API", description = "커뮤니티 게시글 조회 관련 API")
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class CommunityBoardQueryApiController {

    private final CommunityBoardQueryUseCase communityBoardQueryUseCase;

    @GetMapping("/community-boards")
    @Operation(summary = "전체 커뮤니티 게시글 조회", description = "전체 커뮤니티 게시글 목록을 조회합니다.")
    public ApiResponse<Page<CommunityBoardResponseDto>> getAll(
            Pageable pageable
    ) {
        return ApiResponse.ok(
                200,
                communityBoardQueryUseCase.getCommunityBoards(pageable.getPageNumber()),
                "전체 커뮤니티 게시글 리스트 조회 성공"
        );
    }

    @GetMapping("/community-boards/{writerId}")
    @Operation(summary = "작성자별 커뮤니티 게시글 조회", description = "작성자별 커뮤니티 게시글 목록을 조회합니다.")
    public ApiResponse<Page<CommunityBoardResponseDto>> getByWriterId(
            @PathVariable UUID writerId,
            Pageable pageable
    ) {
        return ApiResponse.ok(
                200,
                communityBoardQueryUseCase.getCommunityBoardsByWriterId(writerId, pageable.getPageNumber()),
                "작성자별 커뮤니티 게시글 리스트 조회 성공"
        );
    }

    @GetMapping("/community-board/{id}")
    @Operation(summary = "커뮤니티 게시글 상세 조회", description = "커뮤니티 게시글의 상세 정보를 조회합니다.")
    public ApiResponse<CommunityBoardDetailResponseDto> getById(
            @PathVariable Long id
    ) {
        return ApiResponse.ok(
                200,
                communityBoardQueryUseCase.getCommunityBoardDetail(id),
                "커뮤니티 게시글 상세 조회 성공"
        );
    }
}
