package com.somemore.domains.note.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.domains.note.domain.Note;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record SendNoteToVolunteerRequestDto(

        @Schema(description = "쪽지 수신 봉사자 아이디", example = "123e4567-e89b-12d3-a456-426614174000")
        @NotNull(message = "수신 봉사자 아이디는 필수 값입니다.")
        UUID receiverId,

        @Schema(description = "쪽지 제목", example = "서울 도서관입니다. 답변 드립니다")
        @NotNull(message = "쪽지 제목은 필수 값입니다.")
        String title,

        @Schema(description = "쪽지 내용", example = "~~~~한 일을 할 것 같습니다.")
        @NotNull(message = "쪽지 내용은 필수 값입니다.")
        String content
) {
    public Note toEntity(UUID senderId){
        return Note.create(senderId, receiverId, title, content);
    }
}