package com.somemore.note.service;

import com.somemore.note.repository.NoteRepository;
import com.somemore.note.repository.mapper.NoteReceiverViewForCenter;
import com.somemore.note.usecase.NoteQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class NoteQueryService implements NoteQueryUseCase {

    private final NoteRepository noteRepository;

    @Override
    public Page<NoteReceiverViewForCenter> getNotesForCenter(UUID centerId, Pageable pageable) {
        return noteRepository.findNotesByReceiverIsCenter(centerId, pageable);
    }
}
