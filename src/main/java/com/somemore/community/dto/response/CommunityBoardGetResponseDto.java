package com.somemore.community.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.community.domain.CommunityBoardWithNickname;

import java.time.LocalDateTime;

@JsonNaming(SnakeCaseStrategy.class)
public record CommunityBoardGetResponseDto(
        Long id,
        String title,
        String writerNickname,
        LocalDateTime createdAt
) {
    public static CommunityBoardGetResponseDto fromEntity(CommunityBoardWithNickname board) {
        return new CommunityBoardGetResponseDto(
                board.getCommunityBoard().getId(),
                board.getCommunityBoard().getTitle(),
                board.getWriterNickname(),
                board.getCommunityBoard().getCreatedAt()
        );
    }
}

