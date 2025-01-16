package com.somemore.domains.note.repository;

import com.somemore.domains.note.domain.Note;
import com.somemore.domains.note.repository.mapper.NoteDetailViewForCenter;
import com.somemore.domains.note.repository.mapper.NoteDetailViewForVolunteer;
import com.somemore.domains.note.repository.mapper.NoteReceiverViewForCenter;
import com.somemore.domains.note.repository.mapper.NoteReceiverViewForVolunteer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface NoteRepository {
    Note save(Note note);
    Optional<Note> findById(Long noteId);
    Page<NoteReceiverViewForCenter> findNotesByReceiverIsCenter(UUID centerId, Pageable pageable);
    Page<NoteReceiverViewForVolunteer> findNotesByReceiverIsVolunteer(UUID volunteerId, Pageable pageable);
    Optional<NoteDetailViewForCenter> findNoteDetailViewReceiverIsCenter(Long noteId);
    Optional<NoteDetailViewForVolunteer> findNoteDetailViewReceiverIsVolunteer(Long noteId);
}
