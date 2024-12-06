package com.somemore.note.controller;

import com.somemore.ControllerTestSupport;
import com.somemore.WithMockCustomUser;
import com.somemore.note.dto.SendNoteToCenterRequestDto;
import com.somemore.note.dto.SendNoteToVolunteerRequestDto;
import com.somemore.note.usecase.SendNoteToCenterUseCase;
import com.somemore.note.usecase.SendNoteToVolunteerUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class NoteCommandApiControllerTest extends ControllerTestSupport{

    @MockBean
    private SendNoteToCenterUseCase sendNoteToCenterUseCase;

    @MockBean
    private SendNoteToVolunteerUseCase sendNoteToVolunteerUseCase;

    @DisplayName("봉사자는 기관에게 쪽지를 보낼수 있다 (controller)")
    @Test
    @WithMockCustomUser
    void sendNoteToCenter_Success() throws Exception {
        // Given
        UUID receiverId = UUID.randomUUID();
        SendNoteToCenterRequestDto requestDto = new SendNoteToCenterRequestDto(
                receiverId,
                "쪽지 제목 문의 드릴게 있습니다.",
                "쪽지 내용"
        );

        when(sendNoteToCenterUseCase.sendNoteToCenter(any(UUID.class), any(SendNoteToCenterRequestDto.class)))
                .thenReturn(1L);

        // When & Then
        mockMvc.perform(post("/api/note/volunteer-to-center")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.data").value(1L))
                .andExpect(jsonPath("$.message").value("쪽지 송신 성공"));
    }

    @DisplayName("쪽지 송신 시 누락된 정보가 있다면 송신을 할 수 없다")
    @Test
    void sendNoteToCenter_ValidationFail() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();
        SendNoteToCenterRequestDto invalidRequestDto = new SendNoteToCenterRequestDto(
                null,
                null,
                null
        );

        // When & Then
        mockMvc.perform(post("/api/note/volunteer-to-center")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDto))
                        .requestAttr("userId", userId))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("기관은 봉사자에게 쪽지를 보낼수 있다 (controller)")
    @Test
    @WithMockCustomUser(role = "CENTER")
    void sendNoteToVolunteer_Success() throws Exception {
        // Given
        UUID receiverId = UUID.randomUUID();
        SendNoteToCenterRequestDto requestDto = new SendNoteToCenterRequestDto(
                receiverId,
                "쪽지 제목 문의 드릴게 있습니다.",
                "쪽지 내용"
        );

        when(sendNoteToVolunteerUseCase.sendNoteToVolunteer(any(UUID.class), any(SendNoteToVolunteerRequestDto.class)))
                .thenReturn(1L);

        // When & Then
        mockMvc.perform(post("/api/note/center-to-volunteer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.data").value(1L))
                .andExpect(jsonPath("$.message").value("쪽지 송신 성공"));
    }

}
