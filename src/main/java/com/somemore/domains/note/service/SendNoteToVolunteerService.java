package com.somemore.domains.note.service;

import com.somemore.domains.note.domain.Note;
import com.somemore.domains.note.dto.SendNoteToVolunteerRequestDto;
import com.somemore.domains.note.repository.NoteRepository;
import com.somemore.domains.note.usecase.SendNoteToVolunteerUseCase;
import com.somemore.volunteer.usecase.NEWVolunteerQueryUseCase;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
@Transactional
public class SendNoteToVolunteerService implements SendNoteToVolunteerUseCase {

    private final NEWVolunteerQueryUseCase volunteerQueryUseCase;
    private final NoteRepository noteRepository;

    @Override
    public Long sendNoteToVolunteer(UUID senderId, SendNoteToVolunteerRequestDto requestDto) {
        volunteerQueryUseCase.validateExistsById(requestDto.receiverId());

        Note note = requestDto.toEntity(senderId);
        noteRepository.save(note);
        return note.getId();
    }
}
