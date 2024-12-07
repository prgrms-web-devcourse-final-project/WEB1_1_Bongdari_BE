package com.somemore.note.repository.mapper;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public record NoteReceiverViewForVolunteer(
        @Schema(description = "쪽지 ID", example = "1111")
        Long id,
        @Schema(description = "쪽지 제목", example = "답변 드립니다..")
        String title,
        @Schema(description = "송신한 기관 id", example = "1342134-32423-35345")
        UUID senderId,
        @Schema(description = "송신 기관 닉네임", example = "서울 도서관")
        String senderName,
        @Schema(description = "읽음 여부", example = "true = 읽음, false = 안읽음")
        boolean isRead
) {
}
