package com.somemore.domains.search.controller;

import com.somemore.domains.recruitboard.dto.condition.RecruitBoardNearByCondition;
import com.somemore.domains.recruitboard.dto.condition.RecruitBoardSearchCondition;
import com.somemore.domains.recruitboard.dto.response.RecruitBoardDetailResponseDto;
import com.somemore.domains.recruitboard.dto.response.RecruitBoardWithCenterResponseDto;
import com.somemore.domains.search.usecase.RecruitBoardDocumentUseCase;
import com.somemore.support.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static com.somemore.domains.recruitboard.domain.VolunteerCategory.ADMINISTRATIVE_SUPPORT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RecruitBoardSearchApiControllerTest extends ControllerTestSupport {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecruitBoardDocumentUseCase recruitBoardDocumentUseCase;

    @Test
    @DisplayName("모집글을 검색 조건으로 페이징 조회 성공")
    void getAllBySearch() throws Exception {
        // given
        Page<RecruitBoardWithCenterResponseDto> page = new PageImpl<>(Collections.emptyList());

        given(recruitBoardDocumentUseCase.getRecruitBoardBySearch(any(RecruitBoardSearchCondition.class)))
                .willReturn(page);

        // when
        // then
        mockMvc.perform(get("/api/recruit-boards/search")
                        .param("keyword", "volunteer")
                        .param("category", ADMINISTRATIVE_SUPPORT.name())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.message").value("봉사 활동 모집글 검색 조회 성공"));

        verify(recruitBoardDocumentUseCase, times(1)).getRecruitBoardBySearch(any(RecruitBoardSearchCondition.class));
    }

    @Test
    @DisplayName("위치 기반으로 근처 있는 모집글 페이징 조회할 수 있다.")
    void getNearby() throws Exception {
        // given
        Page<RecruitBoardDetailResponseDto> page = new PageImpl<>(Collections.emptyList());
        given(recruitBoardDocumentUseCase.getRecruitBoardsNearbyWithKeyword(
                any(RecruitBoardNearByCondition.class)
        )).willReturn(page);

        // when
        // then
        mockMvc.perform(get("/api/recruit-boards/nearby")
                        .param("latitude", "37.5665")
                        .param("longitude", "126.9780")
                        .param("radius", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.message").value("근처 봉사 활동 모집글 조회 성공"));

        verify(recruitBoardDocumentUseCase, times(1)).getRecruitBoardsNearbyWithKeyword(
                any(RecruitBoardNearByCondition.class));
    }
}
