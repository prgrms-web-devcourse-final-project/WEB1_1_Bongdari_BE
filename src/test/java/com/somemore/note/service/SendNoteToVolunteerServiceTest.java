package com.somemore.note.service;

import com.somemore.IntegrationTestSupport;
import com.somemore.note.domain.Note;
import com.somemore.note.dto.SendNoteToVolunteerRequestDto;
import com.somemore.note.repository.NoteJpaRepository;
import com.somemore.volunteer.domain.Volunteer;
import com.somemore.volunteer.repository.VolunteerJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.somemore.global.auth.oauth.OAuthProvider.NAVER;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
class SendNoteToVolunteerServiceTest extends IntegrationTestSupport {

    @Autowired
    private SendNoteToVolunteerService sendNoteToVolunteerService;

    @Autowired
    private NoteJpaRepository noteJpaRepository;

    @Autowired
    private VolunteerJpaRepository volunteerJpaRepository;

    @DisplayName("기관은 봉사자에게 쪽지를 보낼수 있다. (service)")
    @Test
    void sendNoteToVolunteer() {
        //given
        Volunteer volunteer = createVolunteer();
        UUID senderId = volunteer.getId(); // 기관 ID
        UUID receiverId = volunteer.getId(); // 봉사자 ID
        String title = "안녕하세요, 서울 도서관입니다.";
        String content = "문의 주신 내용 답장드립니다.";
        SendNoteToVolunteerRequestDto requestDto = new SendNoteToVolunteerRequestDto(receiverId, title, content);

        //when
        Long noteId = sendNoteToVolunteerService.sendNoteToVolunteer(senderId, requestDto);

        //then
        Note savedNote = noteJpaRepository.findById(noteId).orElseThrow(() ->
                new IllegalStateException("쪽지가 저장되지 않았습니다."));
        assertEquals(senderId, savedNote.getSenderId(), "발신자가 올바르지 않습니다.");
        assertEquals(receiverId, savedNote.getReceiverId(), "수신자가 올바르지 않습니다.");
        assertEquals(title, savedNote.getTitle(), "쪽지 제목이 올바르지 않습니다.");
        assertEquals(content, savedNote.getContent(), "쪽지 내용이 올바르지 않습니다.");
        assertFalse(savedNote.getIsRead(), "새로 생성된 쪽지는 읽음 상태가 기본적으로 false여야 합니다.");
    }

    private Volunteer createVolunteer() {
        Volunteer volunteer = Volunteer.createDefault(NAVER, "r1frewgergw");
        return volunteerJpaRepository.save(volunteer);
    }
}
