package com.somemore.community.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.community.repository.mapper.CommunityBoardView;

import java.time.LocalDateTime;

@JsonNaming(SnakeCaseStrategy.class)
public record CommunityBoardGetResponseDto(
        Long id,
        String title,
        String writerNickname,
        LocalDateTime createdAt
) {
    public static CommunityBoardGetResponseDto fromEntity(CommunityBoardView board) {
        return new CommunityBoardGetResponseDto(
                board.communityBoard().getId(),
                board.communityBoard().getTitle(),
                board.writerNickname(),
                board.communityBoard().getCreatedAt()
        );
    }
}

