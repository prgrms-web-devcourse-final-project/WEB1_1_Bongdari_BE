package com.somemore.note.service;

import com.somemore.support.IntegrationTestSupport;
import com.somemore.global.exception.NoSuchElementException;
import com.somemore.note.domain.Note;
import com.somemore.note.repository.NoteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_NOTE;
import static org.junit.jupiter.api.Assertions.*;

class NoteMarkAsReadServiceTest extends IntegrationTestSupport {

    @Autowired
    private NoteMarkAsReadService noteMarkAsReadService;

    @Autowired
    private NoteRepository noteRepository;

    @DisplayName("쪽지를 읽음 처리할 수 있다.")
    @Test
    void noteMarkAsRead() {
        // given
        Note note = Note.create(UUID.randomUUID(), UUID.randomUUID(), "제목", "내용");
        noteRepository.save(note);

        // when
        noteMarkAsReadService.noteMarkAsRead(note.getId());

        // then
        Note updatedNote = noteRepository.findById(note.getId())
                .orElseThrow(() -> new NoSuchElementException(NOT_EXISTS_NOTE));

        assertTrue(updatedNote.getIsRead(), "노트가 읽음 상태로 변경되어야 한다.");
    }

    @DisplayName("존재하지 않는 쪽지에 대한 업데이트 요청은 예외를 반환한다.")
    @Test
    void noteMarkAsRead_throwsException_WhenNoteNotFound() {
        // given
        Long nonExistentNoteId = 999L;

        // when & then
        assertThrows(NoSuchElementException.class,
                () -> noteMarkAsReadService.noteMarkAsRead(nonExistentNoteId),
                "존재하지 않는 노트에 대해 예외가 발생해야 한다.");
    }
}
