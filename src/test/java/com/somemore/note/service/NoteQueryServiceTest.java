package com.somemore.note.service;

import com.somemore.support.IntegrationTestSupport;
import com.somemore.center.domain.Center;
import com.somemore.center.repository.center.CenterJpaRepository;
import com.somemore.global.exception.NoSuchElementException;
import com.somemore.note.domain.Note;
import com.somemore.note.repository.NoteRepository;
import com.somemore.note.repository.mapper.NoteDetailViewForCenter;
import com.somemore.note.repository.mapper.NoteDetailViewForVolunteer;
import com.somemore.note.repository.mapper.NoteReceiverViewForCenter;
import com.somemore.note.repository.mapper.NoteReceiverViewForVolunteer;
import com.somemore.volunteer.domain.Volunteer;
import com.somemore.volunteer.repository.VolunteerJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.somemore.global.auth.oauth.OAuthProvider.NAVER;
import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_NOTE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class NoteQueryServiceTest extends IntegrationTestSupport {

    @Autowired
    private NoteQueryService noteQueryService;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private VolunteerJpaRepository volunteerJpaRepository;

    @Autowired
    private CenterJpaRepository centerJpaRepository;

    @DisplayName("기관은 자신에게 온 쪽지를 페이지 형태로 확인할 수 있다. (Service)")
    @Test
    void getNotesForCenter() {
        //given
        UUID centerId = UUID.randomUUID();
        createTestNotesForCenter(centerId);
        Pageable pageable = PageRequest.of(1, 6);

        //when
        Page<NoteReceiverViewForCenter> result = noteQueryService.getNotesForCenter(centerId, pageable);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(6);
        assertThat(result.getTotalElements()).isEqualTo(15);
        assertThat(result.getContent().getFirst().title()).isEqualTo("Note 9");
    }

    @DisplayName("봉사자는 자신에게 수신된 쪽지를 페이지로 확인할 수 있다. (Service)")
    @Test
    void getNotesForVolunteer() {
        //given
        UUID volunteerId = UUID.randomUUID();
        createTestNotesForVolunteer(volunteerId);
        Pageable pageable = PageRequest.of(1, 6);

        //when
        Page<NoteReceiverViewForVolunteer> result = noteQueryService.getNotesForVolunteer(volunteerId, pageable);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(6);
        assertThat(result.getTotalElements()).isEqualTo(15);
        assertThat(result.getContent().getFirst().title()).isEqualTo("Note 9");
    }

    @DisplayName("기관은 자신에게 온 쪽지의 상세 내용을 확인할 수 있다 (Service)")
    @Test
    void getNoteDetailForCenter() {
        // given
        UUID centerId = UUID.randomUUID();
        Volunteer volunteer = createVolunteer();
        Note note = createNote(centerId, volunteer.getId());

        // when
        NoteDetailViewForCenter result = noteQueryService.getNoteDetailForCenter(note.getId());

        //then
        assertThat(result).isNotNull();
        assertThat(result.noteId()).isEqualTo(note.getId());
        assertThat(result.title()).isEqualTo(note.getTitle());
        assertThat(result.content()).isEqualTo(note.getContent());
        assertThat(result.senderId()).isEqualTo(volunteer.getId());
        assertThat(result.senderName()).isEqualTo(volunteer.getNickname());
        assertThat(result.senderProfileImgLink()).isEqualTo(volunteer.getImgUrl());
    }

    @DisplayName("존재하지 않는 쪽지 조회 시 예외를 던진다 - 기관")
    @Test
    void getNoteDetailForCenter_NotFound() {
        // given
        Long nonExistentNoteId = 9999L;

        // when, then
        assertThatThrownBy(() -> noteQueryService.getNoteDetailForCenter(nonExistentNoteId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage(NOT_EXISTS_NOTE.getMessage());
    }

    @DisplayName("봉사자는 자신에게 온 쪽지의 상세 내용을 확인할 수 있다 (Service)")
    @Test
    void getNoteDetailForVolunteer() {
        // given
        UUID volunteerId = UUID.randomUUID();
        Center center = createCenter();
        Note note = createNote(volunteerId, center.getId());

        // when
        NoteDetailViewForVolunteer result = noteQueryService.getNoteDetailForVolunteer(note.getId());

        //then
        assertThat(result).isNotNull();
        assertThat(result.noteId()).isEqualTo(note.getId());
        assertThat(result.title()).isEqualTo(note.getTitle());
        assertThat(result.content()).isEqualTo(note.getContent());
        assertThat(result.senderId()).isEqualTo(center.getId());
        assertThat(result.senderName()).isEqualTo(center.getName());
        assertThat(result.senderProfileImgLink()).isEqualTo(center.getImgUrl());
    }

    @DisplayName("존재하지 않는 쪽지 조회 시 예외를 던진다 - 봉사자")
    @Test
    void getNoteDetailForVolunteer_NotFound() {
        // given
        Long nonExistentNoteId = 9999L;

        // when, then
        assertThatThrownBy(() -> noteQueryService.getNoteDetailForVolunteer(nonExistentNoteId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage(NOT_EXISTS_NOTE.getMessage());
    }

    private Note createNote(UUID receiverId, UUID senderId){
        Note note = Note.create(senderId, receiverId, "제목", "내용");
        noteRepository.save(note);

        return note;
    }

    private void createTestNotesForCenter(UUID centerId) {
        for (int i = 1; i <= 15; i++) {
            Volunteer volunteer = createVolunteer();
            volunteerJpaRepository.save(volunteer);

            Note note = Note.create(volunteer.getId(), centerId, "Note " + i, "내용");
            noteRepository.save(note);
        }
    }

    private Volunteer createVolunteer() {
        Volunteer volunteer = Volunteer.createDefault(NAVER, UUID.randomUUID().toString());
        return volunteerJpaRepository.save(volunteer);
    }

    private void createTestNotesForVolunteer(UUID volunteerId) {
        for (int i = 1; i <= 15; i++) {
            Center center = createCenter();

            Note note = Note.create(center.getId(), volunteerId, "Note " + i, "내용");
            noteRepository.save(note);
        }
    }

    private Center createCenter() {
        Center center = Center.create(
                "기본 기관 이름",
                "010-1234-5678",
                "http://example.com/image.jpg",
                "기관 소개 내용",
                "http://example.com",
                "account123",
                "password123"
        );

        centerJpaRepository.save(center);

        return center;
    }
}
