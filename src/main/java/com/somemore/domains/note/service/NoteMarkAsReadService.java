package com.somemore.domains.note.service;

import com.somemore.domains.note.repository.NoteRepository;
import com.somemore.domains.note.usecase.NoteMarkAsReadUseCase;
import com.somemore.global.exception.NoSuchElementException;
import com.somemore.domains.note.domain.Note;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_NOTE;

@RequiredArgsConstructor
@Service
@Transactional
public class NoteMarkAsReadService implements NoteMarkAsReadUseCase {

    private final NoteRepository noteRepository;

    @Override
    public void noteMarkAsRead(Long noteId) {
        Note note = getNote(noteId);
        note.markAsRead();
        noteRepository.save(note);
    }

    private Note getNote(Long noteId) {
        return noteRepository.findById(noteId)
                .orElseThrow(() -> new NoSuchElementException(NOT_EXISTS_NOTE));
    }
}
