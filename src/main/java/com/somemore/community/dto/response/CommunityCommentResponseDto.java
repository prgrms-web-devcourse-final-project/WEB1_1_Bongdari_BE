package com.somemore.community.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.community.repository.mapper.CommunityCommentView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CommunityCommentResponseDto(
        Long id,
        String writerNickname,
        String content,
        LocalDateTime updatedAt,
        List<CommunityCommentResponseDto> replies
) {
    public CommunityCommentResponseDto {
        replies = replies == null ? new ArrayList<>() : replies;
    }

    public static CommunityCommentResponseDto fromView(CommunityCommentView comment) {
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
