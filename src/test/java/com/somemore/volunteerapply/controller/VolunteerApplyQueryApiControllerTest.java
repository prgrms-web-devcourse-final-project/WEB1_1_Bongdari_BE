package com.somemore.volunteerapply.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.somemore.ControllerTestSupport;
import com.somemore.volunteerapply.dto.response.VolunteerApplySummaryResponseDto;
import com.somemore.volunteerapply.usecase.VolunteerApplyQueryUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

class VolunteerApplyQueryApiControllerTest extends ControllerTestSupport {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VolunteerApplyQueryUseCase volunteerApplyQueryUseCase;

    @DisplayName("모집글 지원자 통계 조회 성공 테스트")
    @Test
    void getSummaryByRecruitBoardId() throws Exception {
        // given
        Long recruitBoardId = 1L;
        VolunteerApplySummaryResponseDto response = VolunteerApplySummaryResponseDto.builder()
                .total(17L)
                .waiting(5L)
                .approve(10L)
                .reject(2L)
                .build();

        given(volunteerApplyQueryUseCase.getSummaryByRecruitBoardId(recruitBoardId))
                .willReturn(response);

        // when & then
        mockMvc.perform(get("/api/volunteer-apply/recruit-board/{id}/summary", recruitBoardId)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("지원자 통계 조회 성공"))
                .andExpect(jsonPath("$.data.total").value(17))
                .andExpect(jsonPath("$.data.waiting").value(5))
                .andExpect(jsonPath("$.data.approve").value(10))
                .andExpect(jsonPath("$.data.reject").value(2));
    }
}
