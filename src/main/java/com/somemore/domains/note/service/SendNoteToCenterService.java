package com.somemore.domains.note.service;

import com.somemore.center.usecase.NEWCenterQueryUseCase;
import com.somemore.domains.note.domain.Note;
import com.somemore.domains.note.dto.SendNoteToCenterRequestDto;
import com.somemore.domains.note.repository.NoteRepository;
import com.somemore.domains.note.usecase.SendNoteToCenterUseCase;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class SendNoteToCenterService implements SendNoteToCenterUseCase {

    private final NEWCenterQueryUseCase centerQueryUseCase;
    private final NoteRepository noteRepository;

    @Override
    public Long sendNoteToCenter(UUID senderId, SendNoteToCenterRequestDto requestDto) {
        centerQueryUseCase.validateCenterExists(requestDto.receiverId());

        Note note = requestDto.toEntity(senderId);
        noteRepository.save(note);
        return note.getId();
    }

}
