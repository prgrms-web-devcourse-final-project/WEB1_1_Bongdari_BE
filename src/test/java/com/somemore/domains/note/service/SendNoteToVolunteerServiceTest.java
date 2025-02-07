package com.somemore.domains.note.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.somemore.domains.note.domain.Note;
import com.somemore.domains.note.dto.SendNoteToVolunteerRequestDto;
import com.somemore.domains.note.repository.NoteRepository;
import com.somemore.support.IntegrationTestSupport;
import com.somemore.volunteer.domain.NEWVolunteer;
import com.somemore.volunteer.repository.NEWVolunteerRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class SendNoteToVolunteerServiceTest extends IntegrationTestSupport {

    @Autowired
    private SendNoteToVolunteerService sendNoteToVolunteerService;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private NEWVolunteerRepository volunteerRepository;

    private NEWVolunteer volunteer;

    @BeforeEach
    void setUp() {
        volunteer = NEWVolunteer.createDefault(UUID.randomUUID());
        volunteerRepository.save(volunteer);
    }

    @DisplayName("기관은 봉사자에게 쪽지를 보낼수 있다. (service)")
    @Test
    void sendNoteToVolunteer() {
        //given
        UUID senderId = UUID.randomUUID(); // 기관 ID
        UUID receiverId = volunteer.getId(); // 봉사자 ID
        String title = "안녕하세요, 서울 도서관입니다.";
        String content = "문의 주신 내용 답장드립니다.";
        SendNoteToVolunteerRequestDto requestDto = new SendNoteToVolunteerRequestDto(receiverId,
                title, content);

        //when
        Long noteId = sendNoteToVolunteerService.sendNoteToVolunteer(senderId, requestDto);

        //then
        Optional<Note> findNote = noteRepository.findById(noteId);
        assertThat(findNote).isPresent();
        assertThat(findNote.get().getSenderId()).isEqualTo(senderId);
        assertThat(findNote.get().getReceiverId()).isEqualTo(receiverId);
        assertThat(findNote.get().getTitle()).isEqualTo(title);
        assertThat(findNote.get().getContent()).isEqualTo(content);
    }

}
