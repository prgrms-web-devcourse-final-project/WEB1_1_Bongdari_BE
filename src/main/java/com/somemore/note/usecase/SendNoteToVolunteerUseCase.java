package com.somemore.note.usecase;

import com.somemore.note.dto.SendNoteToVolunteerRequestDto;

import java.util.UUID;

public interface SendNoteToVolunteerUseCase {
    Long sendNoteToVolunteer(UUID senderId, SendNoteToVolunteerRequestDto requestDto);
}
