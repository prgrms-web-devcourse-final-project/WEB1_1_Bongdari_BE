package com.somemore.community.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.community.domain.CommunityBoard;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CommunityBoardGetDetailResponseDto(
        Long id,
        UUID writerId,
        String title,
        String content,
        String imageUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static CommunityBoardGetDetailResponseDto fromEntity(CommunityBoard board) {
        return new CommunityBoardGetDetailResponseDto(
                board.getId(),
                board.getWriterId(),
                board.getTitle(),
                board.getContent(),
                board.getImgUrl(),
                board.getCreatedAt(),
                board.getUpdatedAt()
        );
    }
}