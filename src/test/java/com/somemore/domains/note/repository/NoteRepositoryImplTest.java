package com.somemore.domains.note.repository;

import com.somemore.domains.center.domain.Center;
import com.somemore.domains.center.repository.center.CenterJpaRepository;
import com.somemore.domains.note.domain.Note;
import com.somemore.domains.note.repository.mapper.NoteDetailViewForCenter;
import com.somemore.domains.note.repository.mapper.NoteDetailViewForVolunteer;
import com.somemore.domains.note.repository.mapper.NoteReceiverViewForCenter;
import com.somemore.domains.note.repository.mapper.NoteReceiverViewForVolunteer;
import com.somemore.domains.volunteer.domain.Volunteer;
import com.somemore.domains.volunteer.repository.VolunteerJpaRepository;
import com.somemore.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static com.somemore.global.auth.oauth.domain.OAuthProvider.NAVER;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class NoteRepositoryImplTest extends IntegrationTestSupport {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private VolunteerJpaRepository volunteerJpaRepository;

    @Autowired
    private CenterJpaRepository centerJpaRepository;

    @DisplayName("기관은 자신에게 수신된 쪽지를 페이지로 확인할 수 있다. (Repository)")
    @Test
    void findNotesByReceiverIsCenter() {
        //given
        UUID centerId = UUID.randomUUID();
        createTestNotesForCenter(centerId);
        Pageable pageable = PageRequest.of(1, 6);

        //when
        Page<NoteReceiverViewForCenter> result = noteRepository.findNotesByReceiverIsCenter(centerId, pageable);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(6);
        assertThat(result.getTotalElements()).isEqualTo(15);
        assertThat(result.getContent().getFirst().title()).isEqualTo("Note 9");
    }

    @DisplayName("봉사자는 자신에게 수신된 쪽지를 페이지로 확인할 수 있다. (Repository)")
    @Test
    void findNotesByReceiverIsVolunteer() {
        //given
        UUID volunteerId = UUID.randomUUID();
        createTestNotesForVolunteer(volunteerId);
        Pageable pageable = PageRequest.of(1, 6);

        //when
        Page<NoteReceiverViewForVolunteer> result = noteRepository.findNotesByReceiverIsVolunteer(volunteerId, pageable);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(6);
        assertThat(result.getTotalElements()).isEqualTo(15);
        assertThat(result.getContent().getFirst().title()).isEqualTo("Note 9");
    }

    @DisplayName("기관은 자신에게 온 쪽지의 상세 내용을 확인할 수 있다 (Repository)")
    @Test
    void findNoteDetailViewReceiverIsCenter() {
        // given
        Center center = createCenter();
        Volunteer volunteer = createVolunteer();
        Note note = createNote(center.getId(), volunteer.getId());

        // when
        Optional<NoteDetailViewForCenter> result = noteRepository.findNoteDetailViewReceiverIsCenter(note.getId());

        // then
        assertThat(result).isPresent();

        NoteDetailViewForCenter detailView = result.get();

        assertThat(detailView.noteId()).isEqualTo(note.getId());
        assertThat(detailView.title()).isEqualTo(note.getTitle());
        assertThat(detailView.content()).isEqualTo(note.getContent());
        assertThat(detailView.senderId()).isEqualTo(volunteer.getId());
        assertThat(detailView.senderName()).isEqualTo(volunteer.getNickname());
        assertThat(detailView.senderProfileImgLink()).isEqualTo(volunteer.getImgUrl());
    }

    @DisplayName("봉사자는 자신에게 온 쪽지의 상세 내용을 확인할 수 있다 (Repository)")
    @Test
    void findNoteDetailViewReceiverIsVolunteer() {
        //given
        Center center = createCenter();
        Volunteer volunteer = createVolunteer();
        Note note = createNote(volunteer.getId(), center.getId());

        //when
        Optional<NoteDetailViewForVolunteer> result = noteRepository.findNoteDetailViewReceiverIsVolunteer(note.getId());

        //then
        assertThat(result).isPresent();

        NoteDetailViewForVolunteer detailView = result.get();

        assertThat(detailView.noteId()).isEqualTo(note.getId());
        assertThat(detailView.title()).isEqualTo(note.getTitle());
        assertThat(detailView.content()).isEqualTo(note.getContent());
        assertThat(detailView.senderId()).isEqualTo(center.getId());
        assertThat(detailView.senderName()).isEqualTo(center.getName());
        assertThat(detailView.senderProfileImgLink()).isEqualTo(center.getImgUrl());
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

}
