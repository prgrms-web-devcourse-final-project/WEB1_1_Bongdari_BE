package com.somemore.domains.search.controller;

import com.somemore.domains.community.dto.response.CommunityBoardResponseDto;
import com.somemore.domains.community.usecase.board.CommunityBoardQueryUseCase;
import com.somemore.domains.search.config.ElasticsearchHealthChecker;
import com.somemore.domains.search.usecase.CommunityBoardDocumentUseCase;
import com.somemore.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Tag(name = "Community Board Search API", description = "커뮤니티 게시글 검색 관련 API")
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class CommunityBoardSearchApiController {

    private final CommunityBoardQueryUseCase communityBoardQueryUseCase;
    private final Optional<CommunityBoardDocumentUseCase> communityBoardDocumentUseCase;
    private final ElasticsearchHealthChecker elasticsearchHealthChecker;

    @GetMapping("/community-boards/search")
    @Operation(summary = "커뮤니티 게시글 키워드 검색", description = "키워드를 포함한 커뮤니티 게시글 목록을 조회합니다.")
    public ApiResponse<Page<CommunityBoardResponseDto>> getCommunityBoardsBySearch(
            @RequestParam String keyword,
            Pageable pageable
    ) {
        if (elasticsearchHealthChecker.isElasticsearchRunning()) {
            return ApiResponse.ok(
                    200,
                    communityBoardDocumentUseCase.get().getCommunityBoardBySearch(keyword, pageable.getPageNumber()),
                    "커뮤니티 게시글 검색 리스트 조회 성공"
            );
        } else {
            return ApiResponse.ok(
                    200,
                    communityBoardQueryUseCase.getCommunityBoards(keyword, pageable.getPageNumber()),
                    "커뮤니티 게시글 검색 리스트 조회 성공"
            );
        }
    }
}
