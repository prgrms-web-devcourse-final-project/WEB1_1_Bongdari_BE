package com.somemore.domains.community.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.domains.community.repository.mapper.CommunityBoardView;
import com.somemore.domains.search.domain.CommunityBoardDocument;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@JsonNaming(SnakeCaseStrategy.class)
@Schema(description = "커뮤니티 게시글 목록 조회 응답 DTO")
public record CommunityBoardResponseDto(
        @Schema(description = "커뮤니티 게시글 ID", example = "12")
        Long id,
        @Schema(description = "커뮤니티 게시글 제목", example = "11/29 OO도서관 봉사 같이 갈 사람 모집합니다.")
        String title,
        @Schema(description = "작성자(봉사자) 닉네임", example = "나는야 봉사왕")
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

    public static CommunityBoardResponseDto fromDocument(CommunityBoardDocument board) {
        return new CommunityBoardResponseDto(
                board.getId(),
                board.getTitle(),
                board.getWriterNickname(),
                board.getCreatedAt()
        );
    }

}

