package com.somemore.domains.note.service;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_NOTE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.somemore.center.domain.NEWCenter;
import com.somemore.center.repository.NEWCenterRepository;
import com.somemore.domains.note.domain.Note;
import com.somemore.domains.note.repository.NoteRepository;
import com.somemore.domains.note.repository.mapper.NoteDetailViewForCenter;
import com.somemore.domains.note.repository.mapper.NoteDetailViewForVolunteer;
import com.somemore.domains.note.repository.mapper.NoteReceiverViewForCenter;
import com.somemore.domains.note.repository.mapper.NoteReceiverViewForVolunteer;
import com.somemore.global.exception.NoSuchElementException;
import com.somemore.support.IntegrationTestSupport;
import com.somemore.user.domain.UserCommonAttribute;
import com.somemore.user.domain.UserRole;
import com.somemore.user.repository.usercommonattribute.UserCommonAttributeRepository;
import com.somemore.volunteer.domain.NEWVolunteer;
import com.somemore.volunteer.repository.NEWVolunteerRepository;
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
class NoteQueryServiceTest extends IntegrationTestSupport {

    @Autowired
    private NoteQueryService noteQueryService;

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

    @DisplayName("기관은 자신에게 온 쪽지를 페이지 형태로 확인할 수 있다. (Service)")
    @Test
    void getNotesForCenter() {
        //given
        UUID centerId = center.getId();
        createTestNotesForCenter(centerId);
        Pageable pageable = PageRequest.of(1, 6);

        //when
        Page<NoteReceiverViewForCenter> result = noteQueryService.getNotesForCenter(centerId,
                pageable);

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
        UUID volunteerId = volunteer.getId();
        createTestNotesForVolunteer(volunteerId);
        Pageable pageable = PageRequest.of(1, 6);

        //when
        Page<NoteReceiverViewForVolunteer> result = noteQueryService.getNotesForVolunteer(
                volunteerId, pageable);

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
        String title = "제목 1";
        UUID centerId = center.getId();
        Note note = createNote(centerId, volunteer.getId(), title);
        noteRepository.save(note);

        // when
        NoteDetailViewForCenter result = noteQueryService.getNoteDetailForCenter(note.getId());

        //then
        assertThat(result).isNotNull();
        assertThat(result.noteId()).isEqualTo(note.getId());
        assertThat(result.title()).isEqualTo(note.getTitle());
        assertThat(result.content()).isEqualTo(note.getContent());
        assertThat(result.senderId()).isEqualTo(volunteer.getId());
        assertThat(result.senderName()).isEqualTo(volunteer.getNickname());
        assertThat(result.senderProfileImgLink()).isEqualTo(volunteerInfo.getImgUrl());
    }

    @DisplayName("존재하지 않는 쪽지 조회 시 예외를 던진다 - 기관")
    @Test
    void getNoteDetailForCenter_NotFound() {
        // given
        Long nonExistentNoteId = 9999L;

        // when, then
        assertThatThrownBy(
                () -> noteQueryService.getNoteDetailForCenter(nonExistentNoteId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage(NOT_EXISTS_NOTE.getMessage());
    }

    @DisplayName("봉사자는 자신에게 온 쪽지의 상세 내용을 확인할 수 있다 (Service)")
    @Test
    void getNoteDetailForVolunteer() {
        // given
        String title = "제목 2";
        UUID volunteerId = volunteer.getId();
        Note note = createNote(volunteerId, center.getId(), title);
        noteRepository.save(note);

        // when
        NoteDetailViewForVolunteer result = noteQueryService.getNoteDetailForVolunteer(
                note.getId());

        //then
        assertThat(result).isNotNull();
        assertThat(result.noteId()).isEqualTo(note.getId());
        assertThat(result.title()).isEqualTo(note.getTitle());
        assertThat(result.content()).isEqualTo(note.getContent());
        assertThat(result.senderId()).isEqualTo(center.getId());
        assertThat(result.senderName()).isEqualTo(centerInfo.getName());
        assertThat(result.senderProfileImgLink()).isEqualTo(centerInfo.getImgUrl());
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
