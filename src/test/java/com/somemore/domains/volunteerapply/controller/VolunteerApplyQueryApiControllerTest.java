package com.somemore.domains.volunteerapply.controller;

import com.somemore.domains.volunteerapply.dto.condition.VolunteerApplySearchCondition;
import com.somemore.domains.volunteerapply.dto.response.VolunteerApplyRecruitInfoResponseDto;
import com.somemore.domains.volunteerapply.dto.response.VolunteerApplyWithReviewStatusResponseDto;
import com.somemore.domains.volunteerapply.dto.response.VolunteerApplySummaryResponseDto;
import com.somemore.domains.volunteerapply.dto.response.VolunteerApplyVolunteerInfoResponseDto;
import com.somemore.domains.volunteerapply.usecase.VolunteerApplyQueryFacadeUseCase;
import com.somemore.domains.volunteerapply.usecase.VolunteerApplyQueryUseCase;
import com.somemore.global.exception.NoSuchElementException;
import com.somemore.support.ControllerTestSupport;
import com.somemore.support.annotation.WithMockCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collections;
import java.util.UUID;

import static com.somemore.domains.volunteerapply.domain.ApplyStatus.WAITING;
import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_VOLUNTEER_APPLY;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VolunteerApplyQueryApiControllerTest extends ControllerTestSupport {

    @MockBean
    private VolunteerApplyQueryUseCase volunteerApplyQueryUseCase;

    @MockBean
    private VolunteerApplyQueryFacadeUseCase volunteerApplyQueryFacadeUseCase;


    @DisplayName("특정 모집글 봉사자 지원 단건 조회 성공 테스트")
    @Test
    void getVolunteerApplyByRecruitIdAndVolunteerId() throws Exception {
        // given
        Long recruitBoardId = 1L;
        UUID volunteerId = UUID.randomUUID();

        VolunteerApplyWithReviewStatusResponseDto response = VolunteerApplyWithReviewStatusResponseDto.builder()
                .id(1L)
                .volunteerId(volunteerId)
                .recruitBoardId(recruitBoardId)
                .status(WAITING)
                .attended(false)
                .build();

        given(volunteerApplyQueryFacadeUseCase.getVolunteerApplyByRecruitIdAndVolunteerId(recruitBoardId,
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

    @DisplayName("특정 모집글 봉사자 지원 단건 조회 성공 테스트 - 지원 내역이 없는 경우")
    @Test
    void getVolunteerApplyByRecruitIdAndVolunteerIdWhenDoesNotExist() throws Exception {
        // given
        Long recruitBoardId = 1L;
        UUID volunteerId = UUID.randomUUID();

        given(volunteerApplyQueryFacadeUseCase.getVolunteerApplyByRecruitIdAndVolunteerId(recruitBoardId,
                volunteerId))
                .willThrow(new NoSuchElementException(NOT_EXISTS_VOLUNTEER_APPLY));

        // when & then
        mockMvc.perform(
                        get("/api/volunteer-apply/recruit-board/{recruitBoardId}/volunteer/{volunteerId}",
                                recruitBoardId, volunteerId)
                                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(210))
                .andExpect(jsonPath("$.message").value("지원 내역이 없습니다."));
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

    @Test
    @DisplayName("지원자 리스트를 조회 성공 테스트")
    @WithMockCustomUser(role = "CENTER")
    void getVolunteerApplies() throws Exception {
        // given
        Long recruitBoardId = 1L;
        Page<VolunteerApplyVolunteerInfoResponseDto> page = new PageImpl<>(Collections.emptyList());

        given(volunteerApplyQueryFacadeUseCase.getVolunteerAppliesByRecruitIdAndCenterId(
                any(), any(UUID.class), any(VolunteerApplySearchCondition.class)))
                .willReturn(page);

        // when
        // then
        mockMvc.perform(get("/api/volunteer-applies/recruit-board/{id}", recruitBoardId)
                        .param("attended", "false")
                        .param("status", WAITING.toString())
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.message").value("지원자 리스트 조회 성공"));
    }

    @Test
    @DisplayName("특정 봉사자 지원 리스트를 조회 성공 테스트")
    void getVolunteerAppliesByVolunteerId() throws Exception {
        // given
        UUID volunteerId = UUID.randomUUID();
        Page<VolunteerApplyRecruitInfoResponseDto> page = new PageImpl<>(Collections.emptyList());

        given(volunteerApplyQueryFacadeUseCase.getVolunteerAppliesByVolunteerId(
                any(UUID.class), any(VolunteerApplySearchCondition.class)))
                .willReturn(page);

        // when
        // then
        mockMvc.perform(get("/api/volunteer-applies/volunteer/{id}", volunteerId.toString())
                        .param("attended", "false")
                        .param("status", WAITING.toString())
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.message").value("봉사 지원 리스트 조회 성공"));
    }
}
