package com.somemore.community.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.community.repository.mapper.CommunityBoardView;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@JsonNaming(SnakeCaseStrategy.class)
@Schema(description = "커뮤니티 게시글 목록 조회 응답 DTO")
public record CommunityBoardResponseDto(
        @Schema(description = "커뮤니티 게시글 ID", example = "12")
        Long id,
        @Schema(description = "커뮤니티 게시글 ID", example = "12")
        String title,
        @Schema(description = "작성자(봉사자) ID", example = "123e4567-e89b-12d3-a456-426614174000")
        String writerNickname,
        @Schema(description = "커뮤니티 게시글 생성 일시", example = "2023-12-02T11:00:00")
        LocalDateTime createdAt
) {
    public static CommunityBoardResponseDto from(CommunityBoardView board) {
        return new CommunityBoardResponseDto(
                board.communityBoard().getId(),
                board.communityBoard().getTitle(),
                board.writerNickname(),
                board.communityBoard().getCreatedAt()
        );
    }
}

