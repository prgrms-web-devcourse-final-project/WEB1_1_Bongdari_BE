package com.somemore.domains.note.service;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_CENTER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.somemore.center.domain.NEWCenter;
import com.somemore.center.repository.NEWCenterRepository;
import com.somemore.domains.note.domain.Note;
import com.somemore.domains.note.dto.SendNoteToCenterRequestDto;
import com.somemore.domains.note.repository.NoteRepository;
import com.somemore.global.exception.NoSuchElementException;
import com.somemore.support.IntegrationTestSupport;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class SendNoteToCenterServiceTest extends IntegrationTestSupport {

    @Autowired
    private SendNoteToCenterService sendNoteToCenterService;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private NEWCenterRepository centerRepository;

    private NEWCenter center;

    @BeforeEach
    void setUp() {
        center = createCenter(UUID.randomUUID());
        centerRepository.save(center);
    }

    @DisplayName("봉사자는 기관에게 쪽지를 보낼 수 있다.")
    @Test
    void sendNoteToCenter() {
        // given
        UUID senderId = UUID.randomUUID(); // 봉사자 ID
        UUID receiverId = center.getId(); // 기관 ID

        String title = "안녕하세요, 봉사 신청 관련 문의드립니다.";
        String content = "안녕하세요, 이번 주말 봉사 일정에 대해 문의드립니다.";
        SendNoteToCenterRequestDto requestDto = new SendNoteToCenterRequestDto(receiverId, title,
                content);

        // when
        Long noteId = sendNoteToCenterService.sendNoteToCenter(senderId, requestDto);

        // then
        Optional<Note> findNote = noteRepository.findById(noteId);
        assertThat(findNote).isPresent();
        assertThat(findNote.get().getSenderId()).isEqualTo(senderId);
        assertThat(findNote.get().getReceiverId()).isEqualTo(receiverId);
        assertThat(findNote.get().getTitle()).isEqualTo(title);
        assertThat(findNote.get().getContent()).isEqualTo(content);
    }

    @DisplayName("기관 아이디가 올바르지 않으면 예외를 반환한다.")
    @Test
    void sendNoteToNonexistentCenter_ThrowsBadRequestException() {
        // given
        UUID senderId = UUID.randomUUID(); // 봉사자 ID
        UUID nonexistentReceiverId = UUID.randomUUID(); // 존재하지 않는 기관 ID
        String title = "안녕하세요, 봉사 신청 관련 문의드립니다.";
        String content = "안녕하세요, 이번 주말 봉사 일정에 대해 문의드립니다.";
        SendNoteToCenterRequestDto requestDto = new SendNoteToCenterRequestDto(
                nonexistentReceiverId, title, content);

        // when
        // then
        assertThatThrownBy(
                () -> sendNoteToCenterService.sendNoteToCenter(senderId, requestDto))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage(NOT_EXISTS_CENTER.getMessage());
    }

    private NEWCenter createCenter(UUID userId) {
        return NEWCenter.createDefault(userId);
    }

}
