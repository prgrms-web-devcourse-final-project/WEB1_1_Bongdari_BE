package com.somemore.note.repository;

import com.somemore.note.domain.Note;
import com.somemore.note.repository.mapper.NoteReceiverViewForCenter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface NoteRepository {
    Note save(Note note);
    Page<NoteReceiverViewForCenter> findNotesByReceiverIsCenter(UUID centerId, Pageable pageable);
}
