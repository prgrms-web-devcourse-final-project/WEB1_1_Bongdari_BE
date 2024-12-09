package com.somemore.recruitboard.controller;

import com.somemore.global.common.response.ApiResponse;
import com.somemore.recruitboard.domain.RecruitStatus;
import com.somemore.recruitboard.domain.VolunteerCategory;
import com.somemore.recruitboard.dto.condition.RecruitBoardNearByCondition;
import com.somemore.recruitboard.dto.condition.RecruitBoardSearchCondition;
import com.somemore.recruitboard.dto.response.RecruitBoardDetailResponseDto;
import com.somemore.recruitboard.dto.response.RecruitBoardWithCenterResponseDto;
import com.somemore.recruitboard.usecase.query.RecruitBoardDocumentUseCase;
import com.somemore.recruitboard.usecase.query.RecruitBoardQueryUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Tag(name = "Recruit Board Search API", description = "봉사 활동 모집 검색 관련 API")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class RecruitBoardSearchApiController {

    private final RecruitBoardDocumentUseCase recruitBoardDocumentUseCase;

    @GetMapping("/recruit-boards/search")
    @Operation(summary = "모집글 검색 조회", description = "검색 조건을 기반으로 모집글을 조회합니다.")
    public ApiResponse<Page<RecruitBoardWithCenterResponseDto>> getAllBySearch(
            @PageableDefault(sort = "created_at", direction = DESC) Pageable pageable,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) VolunteerCategory category,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) Boolean admitted,
            @RequestParam(required = false) RecruitStatus status
    ) {
        RecruitBoardSearchCondition condition = RecruitBoardSearchCondition.builder()
                .keyword(keyword)
                .category(category)
                .region(region)
                .admitted(admitted)
                .status(status)
                .pageable(pageable)
                .build();

        return ApiResponse.ok(
                200,
                recruitBoardDocumentUseCase.getRecruitBoardBySearch(condition),
                "봉사 활동 모집글 검색 조회 성공"
        );
    }

    @GetMapping("/recruit-boards/nearby")
    @Operation(summary = "근처 모집글 조회", description = "주변 반경 내의 봉사 모집글을 조회합니다.")
    public ApiResponse<Page<RecruitBoardDetailResponseDto>> getNearby(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(required = false, defaultValue = "5") double radius,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "RECRUITING") RecruitStatus status,
            @PageableDefault(sort = "created_at", direction = DESC) Pageable pageable
    ) {
        RecruitBoardNearByCondition condition = RecruitBoardNearByCondition.builder()
                .latitude(latitude)
                .longitude(longitude)
                .radius(radius)
                .keyword(keyword)
                .status(status)
                .pageable(pageable)
                .build();

        return ApiResponse.ok(
                200,
                recruitBoardDocumentUseCase.getRecruitBoardsNearbyWithKeyword(condition),
                "근처 봉사 활동 모집글 조회 성공"
        );
    }
}
