package com.somemore.volunteerapply.controller;

import static com.somemore.volunteerapply.domain.ApplyStatus.WAITING;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.somemore.ControllerTestSupport;
import com.somemore.volunteerapply.dto.response.VolunteerApplyResponseDto;
import com.somemore.volunteerapply.dto.response.VolunteerApplySummaryResponseDto;
import com.somemore.volunteerapply.usecase.VolunteerApplyQueryUseCase;
import java.util.UUID;
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

    @DisplayName("특정 모집글 봉사자 지원 단건 조회 성공 테스트")
    @Test
    void getVolunteerApplyByRecruitIdAndVolunteerId() throws Exception {
        // given
        Long recruitBoardId = 1L;
        UUID volunteerId = UUID.randomUUID();

        VolunteerApplyResponseDto response = VolunteerApplyResponseDto.builder()
                .id(1L)
                .volunteerId(volunteerId)
                .recruitBoardId(recruitBoardId)
                .status(WAITING)
                .attended(false)
                .build();

        given(volunteerApplyQueryUseCase.getVolunteerApplyByRecruitIdAndVolunteerId(recruitBoardId,
                volunteerId))
                .willReturn(response);

        // when & then
        mockMvc.perform(
                        get("/api/volunteer-apply/recruit-board/{recruitBoardId}/volunteer/{volunteerId}",
                                recruitBoardId, volunteerId)
                                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("특정 모집글에 대한 봉사자 지원 단건 조회 성공"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.volunteer_id").value(volunteerId.toString()))
                .andExpect(jsonPath("$.data.recruit_board_id").value(recruitBoardId))
                .andExpect(jsonPath("$.data.status").value("WAITING"))
                .andExpect(jsonPath("$.data.attended").value(false));
    }

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

        given(volunteerApplyQueryUseCase.getSummaryByRecruitId(recruitBoardId))
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
