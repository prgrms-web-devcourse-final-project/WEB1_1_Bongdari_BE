package com.somemore.volunteerapply.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.somemore.ControllerTestSupport;
import com.somemore.WithMockCustomUser;
import com.somemore.volunteerapply.usecase.ApproveVolunteerApplyUseCase;
import com.somemore.volunteerapply.usecase.RejectVolunteerApplyUseCase;
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
}
