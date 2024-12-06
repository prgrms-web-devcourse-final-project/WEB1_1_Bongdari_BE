package com.somemore.note.repository;

import com.somemore.note.domain.Note;

public interface NoteRepository {

    Note save(Note note);
}
