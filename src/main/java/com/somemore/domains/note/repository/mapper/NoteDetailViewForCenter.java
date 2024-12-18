package com.somemore.domains.note.repository.mapper;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record NoteDetailViewForCenter(
        Long noteId,
        String title,
        String content,
        UUID senderId,
        String senderName,
        String senderProfileImgLink,
        LocalDateTime createdAt
) {
}
