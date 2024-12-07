package com.somemore.note.usecase;

import com.somemore.note.repository.mapper.NoteReceiverViewForCenter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface NoteQueryUseCase {
    Page<NoteReceiverViewForCenter> getNotesForCenter(UUID centerId, Pageable pageable);
}
