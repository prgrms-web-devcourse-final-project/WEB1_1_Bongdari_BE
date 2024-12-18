package com.somemore.domains.note.usecase;

import com.somemore.domains.note.dto.SendNoteToCenterRequestDto;

import java.util.UUID;

public interface SendNoteToCenterUseCase {
    Long sendNoteToCenter(UUID senderId, SendNoteToCenterRequestDto requestDto);
}
