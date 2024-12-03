package com.somemore.community.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.somemore.ControllerTestSupport;
import com.somemore.community.dto.response.CommunityCommentResponseDto;
import java.util.Collections;

import com.somemore.community.usecase.comment.CommunityCommentQueryUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

public class CommunityCommentQueryApiControllerTest extends ControllerTestSupport {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommunityCommentQueryUseCase communityCommentQueryUseCase;

    @Test
    @DisplayName("커뮤니티 댓글 조회 성공")
    void getByBoardId() throws Exception {
        //given
        long communityBoardId = 1L;
        Page<CommunityCommentResponseDto> page = new PageImpl<>(Collections.emptyList());

        given(communityCommentQueryUseCase.getCommunityCommentsByBoardId(any(), anyInt()))
                .willReturn(page);

        //when
        //then
        mockMvc.perform(get("/api/community-board/{boardId}/comments", communityBoardId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.message")
                        .value("커뮤니티 게시글의 댓글 리스트 조회 성공"));

        verify(communityCommentQueryUseCase, times(1))
                .getCommunityCommentsByBoardId(any(), anyInt());
    }

}
