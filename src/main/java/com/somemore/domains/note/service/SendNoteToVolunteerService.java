package com.somemore.domains.note.service;

import com.somemore.domains.note.repository.NoteRepository;
import com.somemore.domains.note.usecase.SendNoteToVolunteerUseCase;
import com.somemore.domains.volunteer.usecase.VolunteerQueryUseCase;
import com.somemore.domains.note.domain.Note;
import com.somemore.domains.note.dto.SendNoteToVolunteerRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@RequiredArgsConstructor
@Service
@Transactional
public class SendNoteToVolunteerService implements SendNoteToVolunteerUseCase {

    private final VolunteerQueryUseCase volunteerQueryUseCase;
    private final NoteRepository noteRepository;

    @Override
    public Long sendNoteToVolunteer(UUID senderId, SendNoteToVolunteerRequestDto requestDto) {
        volunteerQueryUseCase.validateVolunteerExists(senderId);

        Note note = requestDto.toEntity(senderId);
        noteRepository.save(note);
        return note.getId();
    }
}
