package com.somemore.domains.note.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.somemore.center.domain.NEWCenter;
import com.somemore.center.repository.NEWCenterRepository;
import com.somemore.domains.note.domain.Note;
import com.somemore.domains.note.repository.mapper.NoteDetailViewForCenter;
import com.somemore.domains.note.repository.mapper.NoteDetailViewForVolunteer;
import com.somemore.domains.note.repository.mapper.NoteReceiverViewForCenter;
import com.somemore.domains.note.repository.mapper.NoteReceiverViewForVolunteer;
import com.somemore.support.IntegrationTestSupport;
import com.somemore.user.domain.UserCommonAttribute;
import com.somemore.user.domain.UserRole;
import com.somemore.user.repository.usercommonattribute.UserCommonAttributeRepository;
import com.somemore.volunteer.domain.NEWVolunteer;
import com.somemore.volunteer.repository.NEWVolunteerRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class NoteRepositoryImplTest extends IntegrationTestSupport {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private NEWVolunteerRepository volunteerRepository;

    @Autowired
    private NEWCenterRepository centerRepository;

    @Autowired
    private UserCommonAttributeRepository userCommonAttributeRepository;

    private NEWCenter center;
    private UserCommonAttribute centerInfo;
    private NEWVolunteer volunteer;
    private UserCommonAttribute volunteerInfo;

    @BeforeEach
    void setUp() {
        center = NEWCenter.createDefault(UUID.randomUUID());
        centerRepository.save(center);

        volunteer = NEWVolunteer.createDefault(UUID.randomUUID());
        volunteerRepository.save(volunteer);

        centerInfo = UserCommonAttribute.createDefault(center.getUserId(), UserRole.CENTER);
        volunteerInfo = UserCommonAttribute.createDefault(volunteer.getUserId(),
                UserRole.VOLUNTEER);

        userCommonAttributeRepository.save(centerInfo);
        userCommonAttributeRepository.save(volunteerInfo);
    }

    @DisplayName("기관은 자신에게 수신된 쪽지를 페이지로 확인할 수 있다. (Repository)")
    @Test
    void findNotesByReceiverIsCenter() {
        //given
        UUID centerId = center.getId();
        createTestNotesForCenter(centerId);
        Pageable pageable = PageRequest.of(1, 6);

        //when
        Page<NoteReceiverViewForCenter> result = noteRepository.findNotesByReceiverIsCenter(
                centerId, pageable);

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
        UUID volunteerId = volunteer.getId();
        createTestNotesForVolunteer(volunteerId);
        Pageable pageable = PageRequest.of(1, 6);

        //when
        Page<NoteReceiverViewForVolunteer> result = noteRepository.findNotesByReceiverIsVolunteer(
                volunteerId, pageable);

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
        String title = "쪽지 제목1";
        Note note = createNote(center.getId(), volunteer.getId(), title);
        noteRepository.save(note);

        // when
        Optional<NoteDetailViewForCenter> result = noteRepository.findNoteDetailViewReceiverIsCenter(
                note.getId());

        // then
        assertThat(result).isPresent();

        NoteDetailViewForCenter detailView = result.get();

        assertThat(detailView.noteId()).isEqualTo(note.getId());
        assertThat(detailView.title()).isEqualTo(note.getTitle());
        assertThat(detailView.content()).isEqualTo(note.getContent());
        assertThat(detailView.senderId()).isEqualTo(volunteer.getId());
        assertThat(detailView.senderName()).isEqualTo(volunteer.getNickname());
        assertThat(detailView.senderProfileImgLink()).isEqualTo(volunteerInfo.getImgUrl());
    }

    @DisplayName("봉사자는 자신에게 온 쪽지의 상세 내용을 확인할 수 있다 (Repository)")
    @Test
    void findNoteDetailViewReceiverIsVolunteer() {
        //given
        String title = "쪽지 제목2";
        Note note = createNote(volunteer.getId(), center.getId(), title);
        noteRepository.save(note);

        //when
        Optional<NoteDetailViewForVolunteer> result = noteRepository.findNoteDetailViewReceiverIsVolunteer(
                note.getId());

        //then
        assertThat(result).isPresent();

        NoteDetailViewForVolunteer detailView = result.get();

        assertThat(detailView.noteId()).isEqualTo(note.getId());
        assertThat(detailView.title()).isEqualTo(note.getTitle());
        assertThat(detailView.content()).isEqualTo(note.getContent());
        assertThat(detailView.senderId()).isEqualTo(center.getId());
        assertThat(detailView.senderName()).isEqualTo(centerInfo.getName());
        assertThat(detailView.senderProfileImgLink()).isEqualTo(centerInfo.getImgUrl());
    }

    private Note createNote(UUID receiverId, UUID senderId, String title) {
        return Note.create(senderId, receiverId, title, "내용");
    }

    private void createTestNotesForCenter(UUID centerId) {
        for (int i = 1; i <= 15; i++) {
            Note note = Note.create(volunteer.getId(), centerId, "Note " + i, "내용");
            noteRepository.save(note);
        }
    }

    private void createTestNotesForVolunteer(UUID volunteerId) {
        for (int i = 1; i <= 15; i++) {
            Note note = Note.create(center.getId(), volunteerId, "Note " + i, "내용");
            noteRepository.save(note);
        }
    }

}
