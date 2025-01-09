package com.somemore.domains.search.controller;

import com.somemore.domains.community.dto.response.CommunityBoardResponseDto;
import com.somemore.domains.search.usecase.CommunityBoardDocumentUseCase;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommunityBoardSearchApiControllerTest extends ControllerTestSupport {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommunityBoardDocumentUseCase communityBoardDocumentUseCase;

    @Test
    @DisplayName("게시글을 검색 조건으로 페이징 조회 성공")
    void getBySearch() throws Exception {
        // given
        Page<CommunityBoardResponseDto> page = new PageImpl<>(Collections.emptyList());

        given(communityBoardDocumentUseCase.getCommunityBoardBySearch(any(), anyInt()))
                .willReturn(page);

        // when
        // then
        mockMvc.perform(get("/api/community-boards/search")
                        .param("keyword", "봉사")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.message").value("커뮤니티 게시글 검색 리스트 조회 성공"));

        verify(communityBoardDocumentUseCase, times(1)).getCommunityBoardBySearch(
                any(), anyInt());
    }
}
