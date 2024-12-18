package com.somemore.domains.note.repository;

import com.somemore.domains.note.domain.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteJpaRepository extends JpaRepository<Note, Long> {

}
