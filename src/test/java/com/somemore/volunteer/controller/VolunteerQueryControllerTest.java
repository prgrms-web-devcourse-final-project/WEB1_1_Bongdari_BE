package com.somemore.volunteer.controller;

import com.somemore.ControllerTestSupport;
import com.somemore.volunteer.dto.response.VolunteerResponseDto;
import com.somemore.volunteer.usecase.VolunteerQueryUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VolunteerQueryControllerTest extends ControllerTestSupport {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VolunteerQueryUseCase volunteerQueryUseCase;

    @Test
    @DisplayName("타인의 프로필을 조회할 수 있다.")
    void getVolunteerProfile() throws Exception {
        // given
        UUID volunteerId = UUID.randomUUID();
        VolunteerResponseDto responseDto = createMockVolunteerResponse();

        given(volunteerQueryUseCase.getVolunteerProfile(volunteerId)).willReturn(responseDto);

        // when & then
        mockMvc.perform(get("/profile/{volunteerId}", volunteerId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.message").value("프로필 조회 성공"));

        verify(volunteerQueryUseCase, times(1)).getVolunteerProfile(volunteerId);
    }

    private VolunteerResponseDto createMockVolunteerResponse() {
        return new VolunteerResponseDto(
                UUID.randomUUID().toString(),
                "Test",
                "http://example.com/image.jpg",
                "Ima volunteer!",
                "Red",
                100,
                10,
                null
        );
    }
}