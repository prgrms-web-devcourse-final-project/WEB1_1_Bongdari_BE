package com.somemore.domains.community.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.domains.community.domain.CommunityBoard;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "커뮤니티 게시글 상세 조회 응답 DTO")
public record CommunityBoardDetailResponseDto(
        @Schema(description = "커뮤니티 게시글 ID", example = "12")
        Long id,
        @Schema(description = "작성자(봉사자) ID", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID writerId,
        @Schema(description = "커뮤니티 게시글 제목", example = "11/29 OO도서관 봉사 같이 갈 사람 모집합니다.")
        String title,
        @Schema(description = "커뮤니티 게시글 내용", example = "저 포함 5명이 같이 가면 좋을 거 같아요")
        String content,
        @Schema(description = "커뮤니티 게시글 생성 일시", example = "2023-12-02T11:00:00")
        LocalDateTime createdAt,
        @Schema(description = "커뮤니티 게시글 수정 일시", example = "2023-12-02T11:00:00")
        LocalDateTime updatedAt
) {
    public static CommunityBoardDetailResponseDto from(CommunityBoard board) {
        return new CommunityBoardDetailResponseDto(
                board.getId(),
                board.getWriterId(),
                board.getTitle(),
                board.getContent(),
                board.getCreatedAt(),
                board.getUpdatedAt()
        );
    }
}