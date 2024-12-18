package com.somemore.domains.note.service;

import com.somemore.domains.note.repository.NoteRepository;
import com.somemore.domains.note.repository.mapper.NoteDetailViewForCenter;
import com.somemore.domains.note.repository.mapper.NoteDetailViewForVolunteer;
import com.somemore.domains.note.repository.mapper.NoteReceiverViewForCenter;
import com.somemore.domains.note.repository.mapper.NoteReceiverViewForVolunteer;
import com.somemore.domains.note.usecase.NoteMarkAsReadUseCase;
import com.somemore.domains.note.usecase.NoteQueryUseCase;
import com.somemore.global.exception.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_NOTE;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class NoteQueryService implements NoteQueryUseCase {

    private final NoteMarkAsReadUseCase noteMarkAsReadUseCase;
    private final NoteRepository noteRepository;

    @Override
    public Page<NoteReceiverViewForCenter> getNotesForCenter(UUID centerId, Pageable pageable) {
        return noteRepository.findNotesByReceiverIsCenter(centerId, pageable);
    }

    @Override
    public Page<NoteReceiverViewForVolunteer> getNotesForVolunteer(UUID volunteerId, Pageable pageable) {
        return noteRepository.findNotesByReceiverIsVolunteer(volunteerId, pageable);
    }

    @Transactional
    @Override
    public NoteDetailViewForCenter getNoteDetailForCenter(Long noteId) {
        noteMarkAsReadUseCase.noteMarkAsRead(noteId);

        return noteRepository.findNoteDetailViewReceiverIsCenter(noteId)
                .orElseThrow(() -> new NoSuchElementException(NOT_EXISTS_NOTE));
    }

    @Transactional
    @Override
    public NoteDetailViewForVolunteer getNoteDetailForVolunteer(Long noteId) {
        noteMarkAsReadUseCase.noteMarkAsRead(noteId);

        return noteRepository.findNoteDetailViewReceiverIsVolunteer(noteId)
                .orElseThrow(() -> new NoSuchElementException(NOT_EXISTS_NOTE));
    }

}
