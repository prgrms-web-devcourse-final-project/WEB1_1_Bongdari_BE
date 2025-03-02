package com.somemore.domains.volunteerapply.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.somemore.domains.volunteerapply.dto.request.VolunteerApplyCreateRequestDto;
import com.somemore.domains.volunteerapply.usecase.ApplyVolunteerApplyUseCase;
import com.somemore.domains.volunteerapply.usecase.WithdrawVolunteerApplyUseCase;
import com.somemore.support.ControllerTestSupport;
import com.somemore.support.annotation.MockUser;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

class VolunteerApplyCommandApiControllerTest extends ControllerTestSupport {

    @MockBean
    private ApplyVolunteerApplyUseCase applyVolunteerApplyUseCase;

    @MockBean
    private WithdrawVolunteerApplyUseCase withdrawVolunteerApplyUseCase;

    @Test
    @DisplayName("봉사 활동 지원 성공 테스트")
    @MockUser
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

    @Test
    @DisplayName("봉사 활동 철회 성공 테스트")
    @MockUser
    void withdraw() throws Exception {
        // given
        Long id = 1L;

        willDoNothing().given(withdrawVolunteerApplyUseCase)
                .withdraw(any(), any(UUID.class));

        // when
        mockMvc.perform(delete("/api/volunteer-apply/{id}", id)
                        .header("Authorization", "Bearer access-token"))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(""))
                .andExpect(jsonPath("$.message").value("봉사 활동 지원 철회 성공"));
    }
}
