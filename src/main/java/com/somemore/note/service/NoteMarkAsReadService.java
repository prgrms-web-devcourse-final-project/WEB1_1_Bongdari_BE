package com.somemore.note.service;

import com.somemore.global.exception.NoSuchElementException;
import com.somemore.note.domain.Note;
import com.somemore.note.repository.NoteRepository;
import com.somemore.note.usecase.NoteMarkAsReadUseCase;
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
    }

    private Note getNote(Long noteId) {
        return noteRepository.findById(noteId)
                .orElseThrow(() -> new NoSuchElementException(NOT_EXISTS_NOTE));
    }
}
