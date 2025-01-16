package com.somemore.domains.community.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public record CommunityCommentUpdateRequestDto(
        @Schema(description = "커뮤니티 댓글 내용", example = "저도 함께 하고 싶습니다.")
        @NotBlank(message = "댓글 내용은 필수 값입니다.")
        String content
) {
}
