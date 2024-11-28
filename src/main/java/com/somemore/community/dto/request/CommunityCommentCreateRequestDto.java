package com.somemore.community.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.community.domain.CommunityComment;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public record CommunityCommentCreateRequestDto(
        @Schema(description = "커뮤니티 댓글 내용", example = "저도 함께 하고 싶습니다.")
        @NotBlank(message = "댓글 내용은 필수 값입니다.")
        String content,
        @Schema(description = "부모 댓글의 ID", example = "1234", nullable = true)
        @Nullable
        Long parentCommentId
) {
    public CommunityComment toEntity(UUID writerId) {
        return CommunityComment.builder()
                .writerId(writerId)
                .content(content)
                .parentCommentId(parentCommentId)
                .build();
    }
}
