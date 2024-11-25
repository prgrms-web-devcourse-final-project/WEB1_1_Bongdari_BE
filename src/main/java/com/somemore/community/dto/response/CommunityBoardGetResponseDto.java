package com.somemore.community.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.community.domain.CommunityBoard;

import java.time.LocalDateTime;

@JsonNaming(SnakeCaseStrategy.class)
public record CommunityBoardGetResponseDto(
        Long id,
        String title,
        String writerNickname,
        LocalDateTime createdAt
) {
    public static CommunityBoardGetResponseDto fromEntity(CommunityBoard board, String writerNickname) {
        return new CommunityBoardGetResponseDto(
                board.getId(),
                board.getTitle(),
                writerNickname,
                board.getCreatedAt()
        );
    }
}

