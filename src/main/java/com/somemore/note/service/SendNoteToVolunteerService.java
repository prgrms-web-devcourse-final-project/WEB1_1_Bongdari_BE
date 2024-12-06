package com.somemore.note.service;

import com.somemore.note.domain.Note;
import com.somemore.note.dto.SendNoteToVolunteerRequestDto;
import com.somemore.note.repository.NoteRepository;
import com.somemore.note.usecase.SendNoteToVolunteerUseCase;
import com.somemore.volunteer.usecase.VolunteerQueryUseCase;
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
