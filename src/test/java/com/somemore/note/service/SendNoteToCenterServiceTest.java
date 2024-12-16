package com.somemore.note.service;

import com.somemore.support.IntegrationTestSupport;
import com.somemore.center.domain.Center;
import com.somemore.center.repository.center.CenterRepository;
import com.somemore.global.exception.BadRequestException;
import com.somemore.note.domain.Note;
import com.somemore.note.dto.SendNoteToCenterRequestDto;
import com.somemore.note.repository.NoteJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_CENTER;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
class SendNoteToCenterServiceTest extends IntegrationTestSupport {

    @Autowired
    private SendNoteToCenterService sendNoteToCenterService;

    @Autowired
    private NoteJpaRepository noteJpaRepository;

    @Autowired
    private CenterRepository centerRepository;


    @DisplayName("봉사자는 기관에게 쪽지를 보낼 수 있다.")
    @Test
    void sendNoteToCenter() {
        // given
        Center center = createCenter();
        UUID senderId = UUID.randomUUID(); // 봉사자 ID
        UUID receiverId = center.getId(); // 기관 ID
        String title = "안녕하세요, 봉사 신청 관련 문의드립니다.";
        String content = "안녕하세요, 이번 주말 봉사 일정에 대해 문의드립니다.";
        SendNoteToCenterRequestDto requestDto = new SendNoteToCenterRequestDto(receiverId, title, content);

        // when
        Long noteId = sendNoteToCenterService.sendNoteToCenter(senderId, requestDto);

        // then
        Note savedNote = noteJpaRepository.findById(noteId).orElseThrow(() ->
                new IllegalStateException("쪽지가 저장되지 않았습니다."));
        assertEquals(senderId, savedNote.getSenderId(), "발신자가 올바르지 않습니다.");
        assertEquals(receiverId, savedNote.getReceiverId(), "수신자가 올바르지 않습니다.");
        assertEquals(title, savedNote.getTitle(), "쪽지 제목이 올바르지 않습니다.");
        assertEquals(content, savedNote.getContent(), "쪽지 내용이 올바르지 않습니다.");
        assertFalse(savedNote.getIsRead(), "새로 생성된 쪽지는 읽음 상태가 기본적으로 false여야 합니다.");
    }

    @DisplayName("기관 아이디가 올바르지 않으면 예외를 반환한다.")
    @Test
    void sendNoteToNonexistentCenter_ThrowsBadRequestException() {
        // given
        UUID senderId = UUID.randomUUID(); // 봉사자 ID
        UUID nonexistentReceiverId = UUID.randomUUID(); // 존재하지 않는 기관 ID
        String title = "안녕하세요, 봉사 신청 관련 문의드립니다.";
        String content = "안녕하세요, 이번 주말 봉사 일정에 대해 문의드립니다.";
        SendNoteToCenterRequestDto requestDto = new SendNoteToCenterRequestDto(nonexistentReceiverId, title, content);

        assertFalse(centerRepository.existsById(nonexistentReceiverId), "");

        // when & then
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> sendNoteToCenterService.sendNoteToCenter(senderId, requestDto));

        // 예외 메시지 검증
        assertEquals(NOT_EXISTS_CENTER.getMessage(), exception.getMessage(), "예외 메시지가 올바르지 않습니다.");
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

        centerRepository.save(center);

        return center;
    }

}
