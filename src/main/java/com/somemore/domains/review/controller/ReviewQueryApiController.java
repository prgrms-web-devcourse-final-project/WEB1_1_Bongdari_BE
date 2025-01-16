package com.somemore.domains.review.controller;

import com.somemore.domains.recruitboard.domain.VolunteerCategory;
import com.somemore.domains.review.dto.condition.ReviewSearchCondition;
import com.somemore.domains.review.dto.response.ReviewDetailResponseDto;
import com.somemore.domains.review.dto.response.ReviewDetailWithNicknameResponseDto;
import com.somemore.domains.review.usecase.ReviewQueryUseCase;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Tag(name = "Review Query API", description = "리뷰 조회 API")
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class ReviewQueryApiController {

    private final ReviewQueryUseCase reviewQueryUseCase;

    @Operation(summary = "리뷰 단건 조회", description = "리뷰 ID를 사용하여 단건 리뷰 조회")
    @GetMapping("/review/{id}")
    public ApiResponse<ReviewDetailResponseDto> getById(@PathVariable Long id) {

        return ApiResponse.ok(
                200,
                reviewQueryUseCase.getDetailById(id),
                "리뷰 단건 조회 성공"
        );
    }

    @Operation(summary = "기관별 리뷰 조회", description = "기관 ID를 사용하여 리뷰 조회")
    @GetMapping("/reviews/center/{centerId}")
    public ApiResponse<Page<ReviewDetailWithNicknameResponseDto>> getReviewsByCenterId(
            @PathVariable UUID centerId,
            @PageableDefault(sort = "created_at", direction = DESC) Pageable pageable,
            @RequestParam(required = false) VolunteerCategory category
    ) {
        ReviewSearchCondition condition = ReviewSearchCondition.builder()
                .category(category)
                .pageable(pageable)
                .build();

        return ApiResponse.ok(
                200,
                reviewQueryUseCase.getDetailsWithNicknameByCenterId(centerId, condition),
                "기관 리뷰 리스트 조회 성공"
        );
    }

    @Operation(summary = "봉사자 리뷰 조회", description = "봉사자 ID를 사용하여 리뷰 조회")
    @GetMapping("/reviews/volunteer/{volunteerId}")
    public ApiResponse<Page<ReviewDetailWithNicknameResponseDto>> getReviewsByVolunteerId(
            @PathVariable UUID volunteerId,
            @PageableDefault(sort = "created_at", direction = DESC) Pageable pageable,
            @RequestParam(required = false) VolunteerCategory category
    ) {
        ReviewSearchCondition condition = ReviewSearchCondition.builder()
                .category(category)
                .pageable(pageable)
                .build();

        return ApiResponse.ok(
                200,
                reviewQueryUseCase.getDetailsWithNicknameByVolunteerId(volunteerId, condition),
                "유저 리뷰 리스트 조회 성공"
        );
    }

}
