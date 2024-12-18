package com.somemore.domains.note.usecase;

import com.somemore.domains.note.dto.SendNoteToVolunteerRequestDto;

import java.util.UUID;

public interface SendNoteToVolunteerUseCase {
    Long sendNoteToVolunteer(UUID senderId, SendNoteToVolunteerRequestDto requestDto);
}
