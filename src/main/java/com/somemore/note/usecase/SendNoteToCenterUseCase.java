package com.somemore.note.usecase;

import com.somemore.note.dto.SendNoteToCenterRequestDto;

import java.util.UUID;

public interface SendNoteToCenterUseCase {
    Long sendNoteToCenter(UUID senderId, SendNoteToCenterRequestDto requestDto);
}
