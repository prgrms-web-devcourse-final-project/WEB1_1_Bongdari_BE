package com.somemore.note.repository;

import com.somemore.IntegrationTestSupport;
import com.somemore.note.domain.Note;
import com.somemore.note.repository.mapper.NoteReceiverViewForCenter;
import com.somemore.volunteer.domain.Volunteer;
import com.somemore.volunteer.repository.VolunteerJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.somemore.auth.oauth.OAuthProvider.NAVER;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class NoteRepositoryImplTest extends IntegrationTestSupport {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private VolunteerJpaRepository volunteerJpaRepository;

    private UUID centerId;

    @BeforeEach
    void setUp() {
        centerId = UUID.randomUUID();
        createTestNotes(centerId);
    }

    @DisplayName("기관은 자신에게 수신된 쪽지를 페이지로 확인할 수 있다. (Repository)")
    @Test
    void findNotesByReceiverIsCenter() {
        //given
        Pageable pageable = PageRequest.of(1, 6);

        //when
        Page<NoteReceiverViewForCenter> result = noteRepository.findNotesByReceiverIsCenter(centerId, pageable);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(6);
        assertThat(result.getTotalElements()).isEqualTo(15);
        assertThat(result.getContent().getFirst().title()).isEqualTo("Note 9");
    }

    private void createTestNotes(UUID centerId) {
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
}
