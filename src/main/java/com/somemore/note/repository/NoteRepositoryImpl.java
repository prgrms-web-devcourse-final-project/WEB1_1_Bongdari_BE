package com.somemore.note.repository;

import com.somemore.note.domain.Note;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class NoteRepositoryImpl implements NoteRepository {

    private final NoteJpaRepository noteJpaRepository;

    @Override
    public Note save(Note note) {
        return noteJpaRepository.save(note);
    }

}
