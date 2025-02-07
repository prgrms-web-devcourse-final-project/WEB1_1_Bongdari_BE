package com.somemore.domains.recruitboard.controller;

import com.somemore.domains.recruitboard.dto.condition.RecruitBoardSearchCondition;
import com.somemore.domains.recruitboard.dto.response.RecruitBoardWithCenterResponseDto;
import com.somemore.domains.recruitboard.dto.response.RecruitBoardWithLocationResponseDto;
import com.somemore.domains.recruitboard.usecase.RecruitBoardQueryUseCase;
import com.somemore.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Tag(name = "Recruit Board Query API", description = "봉사 활동 모집 조회 관련 API")
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class RecruitBoardQueryApiController {

    private final RecruitBoardQueryUseCase recruitBoardQueryUseCase;

    @GetMapping("/recruit-board/{id}")
    @Operation(summary = "봉사 모집글 상세 조회", description = "특정 모집글의 상세 정보를 조회합니다.")
    public ApiResponse<RecruitBoardWithLocationResponseDto> getById(
            @PathVariable Long id
    ) {
        return ApiResponse.ok(
                200,
                recruitBoardQueryUseCase.getWithLocationById(id),
                "봉사 활동 모집 상세 조회 성공"
        );
    }

    @GetMapping("/recruit-boards")
    @Operation(summary = "전체 모집글 조회", description = "모든 봉사 모집글 목록을 조회합니다.")
    public ApiResponse<Page<RecruitBoardWithCenterResponseDto>> getAll(
            @PageableDefault(sort = "created_at", direction = DESC)
            Pageable pageable
    ) {
        RecruitBoardSearchCondition condition = RecruitBoardSearchCondition.builder()
                .pageable(pageable)
                .build();

        return ApiResponse.ok(
                200,
                recruitBoardQueryUseCase.getAllWithCenter(condition),
                "봉사 활동 모집글 리스트 조회 성공"
        );
    }
}
