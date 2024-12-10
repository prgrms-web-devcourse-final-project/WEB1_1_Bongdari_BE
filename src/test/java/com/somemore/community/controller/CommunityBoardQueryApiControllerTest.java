package com.somemore.community.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.somemore.ControllerTestSupport;
import com.somemore.community.dto.response.CommunityBoardDetailResponseDto;
import com.somemore.community.dto.response.CommunityBoardResponseDto;
//import com.somemore.community.usecase.board.CommunityBoardDocumentUseCase;
import com.somemore.community.usecase.board.CommunityBoardQueryUseCase;
import java.util.Collections;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

public class CommunityBoardQueryApiControllerTest extends ControllerTestSupport {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommunityBoardQueryUseCase communityBoardQueryUseCase;

//    @MockBean
//    private CommunityBoardDocumentUseCase communityBoardDocumentUseCase;

    @Test
    @DisplayName("커뮤니티 게시글 전체 조회 성공")
    void getAll() throws Exception {
        //given
        Page<CommunityBoardResponseDto> page = new PageImpl<>(Collections.emptyList());

        given(communityBoardQueryUseCase.getCommunityBoards(anyInt()))
                .willReturn(page);

        //when
        //then
        mockMvc.perform(get("/api/community-boards")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.message")
                        .value("전체 커뮤니티 게시글 리스트 조회 성공"));

        verify(communityBoardQueryUseCase, times(1))
                .getCommunityBoards(anyInt());
    }

    @Test
    @DisplayName("작성자별 커뮤니티 게시글 조회 성공")
    void getByWriterId() throws Exception {
        //given
        UUID writerId = UUID.randomUUID();
        Page<CommunityBoardResponseDto> page = new PageImpl<>(Collections.emptyList());

        given(communityBoardQueryUseCase.getCommunityBoardsByWriterId(any(), anyInt()))
                .willReturn(page);

        //when
        //then
        mockMvc.perform(get("/api/community-boards/{writerId}", writerId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.message")
                        .value("작성자별 커뮤니티 게시글 리스트 조회 성공"));

        verify(communityBoardQueryUseCase, times(1))
                .getCommunityBoardsByWriterId(any(), anyInt());
    }

    @Test
    @DisplayName("커뮤니티 게시글 상세 조회 성공")
    void getById() throws Exception {
        //given
        Long communityBoardId = 1L;
        CommunityBoardDetailResponseDto responseDto = CommunityBoardDetailResponseDto.builder().build();
        given(communityBoardQueryUseCase.getCommunityBoardDetail(any()))
                .willReturn(responseDto);

        //when
        //then
        mockMvc.perform(get("/api/community-board/{id}", communityBoardId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.message")
                        .value("커뮤니티 게시글 상세 조회 성공"));

        verify(communityBoardQueryUseCase, times(1))
                .getCommunityBoardDetail(any());
    }

//    @Test
//    @DisplayName("커뮤니티 게시글 검색 조회 성공")
//    void getBySearch() throws Exception {
//        // given
//        Page<CommunityBoardResponseDto> page = new PageImpl<>(Collections.emptyList());
//
//        given(communityBoardDocumentUseCase.getCommunityBoardBySearch(any(), anyInt()))
//                .willReturn(page);
//
//        // when
//        // then
//        mockMvc.perform(get("/api/community-boards/search")
//                        .param("keyword", "봉사")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
//                .andExpect(jsonPath("$.data").exists())
//                .andExpect(jsonPath("$.message").value("커뮤니티 게시글 검색 리스트 조회 성공"));
//
//        verify(communityBoardDocumentUseCase, times(1)).getCommunityBoardBySearch(
//                any(), anyInt());
//    }
}
