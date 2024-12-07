package com.somemore.note.repository;

import com.somemore.note.domain.Note;
import com.somemore.note.repository.mapper.NoteDetailViewForCenter;
import com.somemore.note.repository.mapper.NoteReceiverViewForCenter;
import com.somemore.note.repository.mapper.NoteReceiverViewForVolunteer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface NoteRepository {
    Note save(Note note);
    Page<NoteReceiverViewForCenter> findNotesByReceiverIsCenter(UUID centerId, Pageable pageable);
    Page<NoteReceiverViewForVolunteer> findNotesByReceiverIsVolunteer(UUID volunteerId, Pageable pageable);
    Optional<NoteDetailViewForCenter> findNoteDetailViewReceiverIsCenter(Long noteId);
}
