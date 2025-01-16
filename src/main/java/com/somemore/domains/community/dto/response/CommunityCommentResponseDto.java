package com.somemore.domains.community.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.domains.community.repository.mapper.CommunityCommentView;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CommunityCommentResponseDto(
        @Schema(description = "커뮤니티 댓글 ID", example = "1234")
        Long id,
        @Schema(description = "작성자(봉사자) 닉네임", example = "dkdudab")
        String writerNickname,
        @Schema(description = "커뮤니티 댓글 내용", example = "저도 함께 하고 싶습니다.")
        String content,
        @Schema(description = "커뮤니티 댓글 수정 일시", example = "2023-12-02T11:00:00")
        LocalDateTime updatedAt,
        @Schema(description = "대댓글 목록", example = """
                    [
                        {
                            "id": 3,
                            "content": "첫 번째 댓글의 답글입니다.",
                            "writerNickname": "사용자2",
                            "createdAt": "2023-12-02T11:00:00",
                            "replies": []
                        }
                    ]
                """)
        List<CommunityCommentResponseDto> replies
) {
    public CommunityCommentResponseDto {
        replies = replies == null ? new ArrayList<>() : replies;
    }

    public static CommunityCommentResponseDto from(CommunityCommentView comment) {
        return new CommunityCommentResponseDto(
                comment.communityComment().getId(),
                comment.writerNickname(),
                comment.communityComment().getContent(),
                comment.communityComment().getUpdatedAt(),
                new ArrayList<>()
        );
    }

    public void addReply(CommunityCommentResponseDto reply) {
        this.replies.add(reply);
    }
}
