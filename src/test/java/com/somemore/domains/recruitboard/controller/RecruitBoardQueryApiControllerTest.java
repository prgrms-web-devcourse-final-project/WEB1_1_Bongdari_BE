package com.somemore.domains.recruitboard.controller;

import com.somemore.domains.recruitboard.dto.condition.RecruitBoardNearByCondition;
import com.somemore.domains.recruitboard.dto.condition.RecruitBoardSearchCondition;
import com.somemore.domains.recruitboard.dto.response.RecruitBoardDetailResponseDto;
import com.somemore.domains.recruitboard.dto.response.RecruitBoardResponseDto;
import com.somemore.domains.recruitboard.dto.response.RecruitBoardWithCenterResponseDto;
import com.somemore.domains.recruitboard.dto.response.RecruitBoardWithLocationResponseDto;
import com.somemore.domains.recruitboard.usecase.RecruitBoardQueryUseCase;
import com.somemore.support.ControllerTestSupport;
import com.somemore.support.annotation.MockUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.UUID;

import static com.somemore.domains.recruitboard.domain.VolunteerCategory.ADMINISTRATIVE_SUPPORT;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RecruitBoardQueryApiControllerTest extends ControllerTestSupport {

    @MockBean
    private RecruitBoardQueryUseCase recruitBoardQueryUseCase;

    @Test
    @DisplayName("모집글 ID로 상세 조회할 수 있다.")
    void getById() throws Exception {
        // given
        Long recruitBoardId = 1L;
        var responseDto = RecruitBoardWithLocationResponseDto.builder().build();
        given(recruitBoardQueryUseCase.getWithLocationById(recruitBoardId)).willReturn(responseDto);

        // when
        // then
        mockMvc.perform(get("/api/recruit-board/{id}", recruitBoardId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.message").value("봉사 활동 모집 상세 조회 성공"));

        verify(recruitBoardQueryUseCase, times(1)).getWithLocationById(recruitBoardId);
    }

    @Test
    @DisplayName("모집글 페이징 처리하여 전체 조회 할 수 있다.")
    void getAll() throws Exception {
        // given
        Page<RecruitBoardWithCenterResponseDto> page = new PageImpl<>(Collections.emptyList());

        given(recruitBoardQueryUseCase.getAllWithCenter(any(RecruitBoardSearchCondition.class)))
                .willReturn(page);

        // when
        // then
        mockMvc.perform(get("/api/recruit-boards")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.message").value("봉사 활동 모집글 리스트 조회 성공"));

        verify(recruitBoardQueryUseCase, times(1)).getAllWithCenter(
                any(RecruitBoardSearchCondition.class));
    }

    @Test
    @DisplayName("기관 ID로 모집글 페이징 조회할 수 있다.")
    void getRecruitBoardsByCenterId() throws Exception {
        // given
        UUID centerId = UUID.randomUUID();
        Page<RecruitBoardResponseDto> page = new PageImpl<>(Collections.emptyList());

        given(recruitBoardQueryUseCase.getRecruitBoardsByCenterId(eq(centerId),
                any(RecruitBoardSearchCondition.class)))
                .willReturn(page);

        // when
        // then
        mockMvc.perform(get("/api/recruit-boards/center/{centerId}", centerId)
                        .param("keyword", "volunteer")
                        .param("category", ADMINISTRATIVE_SUPPORT.name())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.message").value("특정 기관 봉사 활동 모집글 조회 성공"));

        verify(recruitBoardQueryUseCase, times(1)).getRecruitBoardsByCenterId(eq(centerId),
                any(RecruitBoardSearchCondition.class));
    }

    @MockUser(role = "ROLE_CENTER")
    @DisplayName("본인(기관)이 작성한 게시글 리스트를 조회할 수 있다.")
    @Test
    void getMyRecruitBoards() throws Exception {
        // given
        Page<RecruitBoardResponseDto> page = new PageImpl<>(Collections.emptyList());

        given(recruitBoardQueryUseCase.getRecruitBoardsByCenterId(any(),
                any(RecruitBoardSearchCondition.class)))
                .willReturn(page);

        // when
        // then
        mockMvc.perform(get("/api/recruit-boards/me")
                        .param("keyword", "volunteer")
                        .param("category", ADMINISTRATIVE_SUPPORT.name())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.message").value("기관 봉사 활동 모집글 조회 성공"));

        verify(recruitBoardQueryUseCase, times(1))
                .getRecruitBoardsByCenterId(any(), any(RecruitBoardSearchCondition.class));
    }
}
