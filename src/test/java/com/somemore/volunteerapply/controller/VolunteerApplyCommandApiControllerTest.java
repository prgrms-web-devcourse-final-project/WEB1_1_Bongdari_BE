package com.somemore.volunteerapply.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.somemore.ControllerTestSupport;
import com.somemore.WithMockCustomUser;
import com.somemore.volunteerapply.dto.VolunteerApplyCreateRequestDto;
import com.somemore.volunteerapply.usecase.ApplyVolunteerApplyUseCase;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

class VolunteerApplyCommandApiControllerTest extends ControllerTestSupport {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ApplyVolunteerApplyUseCase applyVolunteerApplyUseCase;

    @Test
    @DisplayName("봉사 활동 지원 성공 테스트")
    @WithMockCustomUser
    void apply() throws Exception {
        // given
        VolunteerApplyCreateRequestDto dto = VolunteerApplyCreateRequestDto.builder()
                .recruitBoardId(1L)
                .build();

        Long applyId = 1L;

        given(applyVolunteerApplyUseCase.apply(any(), any(UUID.class)))
                .willReturn(applyId);

        String requestBody = objectMapper.writeValueAsString(dto);

        // when
        mockMvc.perform(post("/api/volunteer-apply")
                        .content(requestBody)
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", "Bearer access-token"))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.data").value(applyId))
                .andExpect(jsonPath("$.message").value("봉사 활동 지원 성공"));
    }
}
