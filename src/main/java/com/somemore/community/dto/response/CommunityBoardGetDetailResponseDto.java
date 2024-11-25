package com.somemore.community.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.community.domain.CommunityBoard;

import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CommunityBoardGetDetailResponseDto(
        Long id,
        String title,
        String content,
        String imageUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        WriterDetailDto writerDetailDto
) {
    public static CommunityBoardGetDetailResponseDto fromEntity(CommunityBoard board, WriterDetailDto writer) {
        return new CommunityBoardGetDetailResponseDto(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getImgUrl(),
                board.getCreatedAt(),
                board.getUpdatedAt(),
                writer
        );
    }
}