package com.somemore.volunteerapply.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.somemore.support.ControllerTestSupport;
import com.somemore.support.annotation.WithMockCustomUser;
import com.somemore.volunteerapply.usecase.SettleVolunteerApplyFacadeUseCase;
import com.somemore.volunteerapply.dto.request.VolunteerApplySettleRequestDto;
import com.somemore.volunteerapply.usecase.ApproveVolunteerApplyUseCase;
import com.somemore.volunteerapply.usecase.RejectVolunteerApplyUseCase;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

class CenterVolunteerApplyCommandApiControllerTest extends ControllerTestSupport {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApproveVolunteerApplyUseCase approveVolunteerApplyUseCase;

    @MockBean
    private RejectVolunteerApplyUseCase rejectVolunteerApplyUseCase;

    @MockBean
    private SettleVolunteerApplyFacadeUseCase settleVolunteerApplyFacadeUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("봉사 활동 지원 승인 성공 테스트")
    @WithMockCustomUser(role = "CENTER")
    void approve() throws Exception {
        // given
        Long id = 1L;

        willDoNothing().given(approveVolunteerApplyUseCase)
                .approve(any(), any(UUID.class));
        // when
        mockMvc.perform(patch("/api/volunteer-apply/{id}/approve", id)
                        .header("Authorization", "Bearer access-token"))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(""))
                .andExpect(jsonPath("$.message").value("봉사 활동 지원 승인 성공"));
    }

    @Test
    @DisplayName("봉사 활동 지원 거절 성공 테스트")
    @WithMockCustomUser(role = "CENTER")
    void reject() throws Exception {
        // given
        Long id = 1L;

        willDoNothing().given(rejectVolunteerApplyUseCase)
                .reject(any(), any(UUID.class));
        // when
        mockMvc.perform(patch("/api/volunteer-apply/{id}/reject", id)
                        .header("Authorization", "Bearer access-token"))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(""))
                .andExpect(jsonPath("$.message").value("봉사 활동 지원 거절 성공"));
    }

    @Test
    @DisplayName("봉사 활동 지원 정산 성공 테스트")
    @WithMockCustomUser(role = "CENTER")
    void settle() throws Exception {
        // given
        VolunteerApplySettleRequestDto dto = VolunteerApplySettleRequestDto.builder()
                .ids(List.of(1L, 2L, 3L))
                .build();

        willDoNothing().given(settleVolunteerApplyFacadeUseCase)
                .settleVolunteerApplies(any(VolunteerApplySettleRequestDto.class), any(UUID.class));
        // when
        mockMvc.perform(post("/api/volunteer-applies/settle")
                        .content(objectMapper.writeValueAsBytes(dto))
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", "Bearer access-token"))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(""))
                .andExpect(jsonPath("$.message").value("봉사 활동 지원 정산 성공"));

    }
}
