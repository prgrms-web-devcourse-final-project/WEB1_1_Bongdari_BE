package com.somemore.note.service;

import com.somemore.center.usecase.query.CenterQueryUseCase;
import com.somemore.note.domain.Note;
import com.somemore.note.dto.SendNoteToCenterRequestDto;
import com.somemore.note.repository.NoteRepository;
import com.somemore.note.usecase.SendNoteToCenterUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional
public class SendNoteToCenterService implements SendNoteToCenterUseCase {

    private final CenterQueryUseCase centerQueryUseCase;
    private final NoteRepository noteRepository;

    @Override
    public Long sendNoteToCenter(UUID senderId, SendNoteToCenterRequestDto requestDto) {
        centerQueryUseCase.validateCenterExists(requestDto.receiverId());

        Note note = requestDto.toEntity(senderId);
        noteRepository.save(note);
        return note.getId();
    }

}
