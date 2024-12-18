package com.somemore.domains.community.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public record CommunityBoardUpdateRequestDto(
        @Schema(description = "커뮤니티 게시글 제목", example = "11/29 OO도서관 봉사 같이 갈 사람 모집합니다.")
        @NotBlank(message = "게시글 제목은 필수 값입니다.")
        String title,
        @Schema(description = "커뮤니티 게시글 내용", example = "저 포함 5명이 같이 가면 좋을 거 같아요")
        @NotBlank(message = "게시글 내용은 필수 값입니다.")
        String content
) {}