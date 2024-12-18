package com.somemore.domains.note.usecase;

import com.somemore.domains.note.repository.mapper.NoteDetailViewForCenter;
import com.somemore.domains.note.repository.mapper.NoteDetailViewForVolunteer;
import com.somemore.domains.note.repository.mapper.NoteReceiverViewForCenter;
import com.somemore.domains.note.repository.mapper.NoteReceiverViewForVolunteer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface NoteQueryUseCase {
    Page<NoteReceiverViewForCenter> getNotesForCenter(UUID centerId, Pageable pageable);
    Page<NoteReceiverViewForVolunteer> getNotesForVolunteer(UUID volunteerId, Pageable pageable);
    NoteDetailViewForCenter getNoteDetailForCenter(Long noteId);
    NoteDetailViewForVolunteer getNoteDetailForVolunteer(Long noteId);
}
