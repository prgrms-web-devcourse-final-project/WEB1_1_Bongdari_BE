package com.somemore.note.controller;

import com.somemore.ControllerTestSupport;
import com.somemore.WithMockCustomUser;
import com.somemore.global.exception.NoSuchElementException;
import com.somemore.note.repository.mapper.NoteDetailViewForCenter;
import com.somemore.note.repository.mapper.NoteDetailViewForVolunteer;
import com.somemore.note.repository.mapper.NoteReceiverViewForCenter;
import com.somemore.note.repository.mapper.NoteReceiverViewForVolunteer;
import com.somemore.note.usecase.NoteQueryUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_NOTE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class NoteQueryApiControllerTest extends ControllerTestSupport {

    @MockBean
    private NoteQueryUseCase noteQueryUseCase;

    @DisplayName("기관은 자신에게 온 쪽지를 페이지 형태로 확인할 수 있다. (Controller)")
    @Test
    @WithMockCustomUser(role = "CENTER")
    void getNotesByCenterId() throws Exception {
        // given
        Pageable pageable = PageRequest.of(0, 6);
        List<NoteReceiverViewForCenter> notes = List.of(
                NoteReceiverViewForCenter.builder()
                        .id(1L)
                        .title("문의 드립니다.")
                        .senderName("봉사왕")
                        .isRead(false)
                        .build(),
                NoteReceiverViewForCenter.builder()
                        .id(2L)
                        .title("긴급 문의")
                        .senderName("지원자")
                        .isRead(true)
                        .build()
        );
        Page<NoteReceiverViewForCenter> mockResponse = new PageImpl<>(notes, pageable, notes.size());

        when(noteQueryUseCase.getNotesForCenter(any(UUID.class), eq(pageable)))
                .thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(get("/api/note/center")
                        .param("page", "0")
                        .param("size", "6")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("내 쪽지 조회 성공"))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content[0].id").value(1))
                .andExpect(jsonPath("$.data.content[0].title").value("문의 드립니다."))
                .andExpect(jsonPath("$.data.content[0].sender_name").value("봉사왕"))
                .andExpect(jsonPath("$.data.content[0].is_read").value(false))
                .andExpect(jsonPath("$.data.content[1].id").value(2))
                .andExpect(jsonPath("$.data.content[1].title").value("긴급 문의"))
                .andExpect(jsonPath("$.data.content[1].sender_name").value("지원자"))
                .andExpect(jsonPath("$.data.content[1].is_read").value(true))
                .andExpect(jsonPath("$.data.pageable.pageNumber").value(0))
                .andExpect(jsonPath("$.data.pageable.pageSize").value(6))
                .andExpect(jsonPath("$.data.totalElements").value(2));
    }

    @DisplayName("봉사자는 자신에게 온 쪽지를 페이지 형태로 확인할 수 있다. (Controller)")
    @Test
    @WithMockCustomUser
    void getNotesByVolunteerId() throws Exception {
        // given
        Pageable pageable = PageRequest.of(0, 6);
        List<NoteReceiverViewForVolunteer> notes = List.of(
                NoteReceiverViewForVolunteer.builder()
                        .id(1L)
                        .title("답변 드립니다.")
                        .senderName("서울 도서관")
                        .isRead(false)
                        .build(),
                NoteReceiverViewForVolunteer.builder()
                        .id(2L)
                        .title("요양원 입니다.")
                        .senderName("서울 요양원")
                        .isRead(true)
                        .build()
        );
        Page<NoteReceiverViewForVolunteer> mockResponse = new PageImpl<>(notes, pageable, notes.size());

        when(noteQueryUseCase.getNotesForVolunteer(any(UUID.class), eq(pageable)))
                .thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(get("/api/note/volunteer")
                        .param("page", "0")
                        .param("size", "6")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("내 쪽지 조회 성공"))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content[0].id").value(1))
                .andExpect(jsonPath("$.data.content[0].title").value("답변 드립니다."))
                .andExpect(jsonPath("$.data.content[0].sender_name").value("서울 도서관"))
                .andExpect(jsonPath("$.data.content[0].is_read").value(false))
                .andExpect(jsonPath("$.data.content[1].id").value(2))
                .andExpect(jsonPath("$.data.content[1].title").value("요양원 입니다."))
                .andExpect(jsonPath("$.data.content[1].sender_name").value("서울 요양원"))
                .andExpect(jsonPath("$.data.content[1].is_read").value(true))
                .andExpect(jsonPath("$.data.pageable.pageNumber").value(0))
                .andExpect(jsonPath("$.data.pageable.pageSize").value(6))
                .andExpect(jsonPath("$.data.totalElements").value(2));
    }

    @DisplayName("기관은 특정 쪽지의 상세 내용을 조회할 수 있다. (Controller)")
    @Test
    @WithMockCustomUser(role = "CENTER")
    void getNoteDetailForCenter() throws Exception {
        // given
        Long noteId = 1L;
        NoteDetailViewForCenter mockResponse = new NoteDetailViewForCenter(
                noteId,
                "문의 드립니다.",
                "상세 내용입니다.",
                UUID.randomUUID(),
                "봉사왕",
                "profileImgLinkHere",
                LocalDateTime.now()
        );

        when(noteQueryUseCase.getNoteDetailForCenter(noteId))
                .thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(get("/api/note/center/{noteId}", noteId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("쪽지 상세 조회 성공"))
                .andExpect(jsonPath("$.data.note_id").value(noteId))
                .andExpect(jsonPath("$.data.title").value("문의 드립니다."))
                .andExpect(jsonPath("$.data.content").value("상세 내용입니다."))
                .andExpect(jsonPath("$.data.sender_name").value("봉사왕"))
                .andExpect(jsonPath("$.data.sender_profile_img_link").value("profileImgLinkHere"));
    }

    @DisplayName("봉사자는 특정 쪽지의 상세 내용을 조회할 수 있다. (Controller)")
    @Test
    @WithMockCustomUser
    void getNoteDetailForVolunteer() throws Exception {
        // given
        Long noteId = 1L;
        NoteDetailViewForVolunteer mockResponse = new NoteDetailViewForVolunteer(
                noteId,
                "답변 드립니다.",
                "상세 내용입니다.",
                UUID.randomUUID(),
                "서울 도서관",
                "profileImgLinkHere",
                LocalDateTime.now()
        );

        when(noteQueryUseCase.getNoteDetailForVolunteer(noteId))
                .thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(get("/api/note/volunteer/{noteId}", noteId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("쪽지 상세 조회 성공"))
                .andExpect(jsonPath("$.data.note_id").value(noteId))
                .andExpect(jsonPath("$.data.title").value("답변 드립니다."))
                .andExpect(jsonPath("$.data.content").value("상세 내용입니다."))
                .andExpect(jsonPath("$.data.sender_name").value("서울 도서관"))
                .andExpect(jsonPath("$.data.sender_profile_img_link").value("profileImgLinkHere"));
    }

    @DisplayName("존재하지 않는 쪽지를 기관이 조회할 경우 예외를 반환한다.")
    @Test
    @WithMockCustomUser(role = "CENTER")
    void getNoteDetailForCenter_NotFound() throws Exception {
        // given
        Long nonExistentNoteId = 9999L;

        when(noteQueryUseCase.getNoteDetailForCenter(nonExistentNoteId))
                .thenThrow(new NoSuchElementException(NOT_EXISTS_NOTE));

        // When & Then
        mockMvc.perform(get("/api/note/center/{noteId}", nonExistentNoteId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.title").value("데이터가 존재하지 않음"))
                .andExpect(jsonPath("$.detail").value("존재하지 않는 쪽지입니다."));
    }

    @DisplayName("존재하지 않는 쪽지를 봉사자가 조회할 경우 예외를 반환한다.")
    @Test
    @WithMockCustomUser
    void getNoteDetailForVolunteer_NotFound() throws Exception {
        // given
        Long nonExistentNoteId = 9999L;

        when(noteQueryUseCase.getNoteDetailForVolunteer(nonExistentNoteId))
                .thenThrow(new NoSuchElementException(NOT_EXISTS_NOTE));

        // When & Then
        mockMvc.perform(get("/api/note/volunteer/{noteId}", nonExistentNoteId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.title").value("데이터가 존재하지 않음"))
                .andExpect(jsonPath("$.detail").value("존재하지 않는 쪽지입니다."));
    }

}