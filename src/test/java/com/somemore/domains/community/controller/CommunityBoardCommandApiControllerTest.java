package com.somemore.domains.community.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.somemore.domains.community.dto.request.CommunityBoardCreateRequestDto;
import com.somemore.domains.community.dto.request.CommunityBoardUpdateRequestDto;
import com.somemore.domains.community.usecase.board.CreateCommunityBoardUseCase;
import com.somemore.domains.community.usecase.board.DeleteCommunityBoardUseCase;
import com.somemore.domains.community.usecase.board.UpdateCommunityBoardUseCase;
import com.somemore.support.ControllerTestSupport;
import com.somemore.support.annotation.MockUser;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

public class CommunityBoardCommandApiControllerTest extends ControllerTestSupport {

    @MockBean
    private CreateCommunityBoardUseCase createCommunityBoardUseCase;

    @MockBean
    private UpdateCommunityBoardUseCase updateCommunityBoardUseCase;

    @MockBean
    private DeleteCommunityBoardUseCase deleteCommunityBoardUseCase;

    @Test
    @DisplayName("커뮤니티 게시글 등록 성공 테스트")
    @MockUser
    void createCommunityBoard_success() throws Exception {
        //given
        CommunityBoardCreateRequestDto requestDto = CommunityBoardCreateRequestDto.builder()
                .title("11/29 OO도서관 봉사 같이 갈 사람 모집합니다.")
                .content("저 포함 5명이 같이 가면 좋을 거 같아요.")
                .build();

        String requestBody = objectMapper.writeValueAsString(requestDto);
        long communityBoardId = 1L;

        given(createCommunityBoardUseCase.createCommunityBoard(any(), any(UUID.class)))
                .willReturn(communityBoardId);

        //when
        mockMvc.perform(post("/api/community-board")
                        .content(requestBody)
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", "Bearer access-token"))
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.data").value(communityBoardId))
                .andExpect(jsonPath("$.message").value("커뮤니티 게시글 등록 성공"));
    }

    @Test
    @DisplayName("커뮤니티 게시글 수정 성공 테스트")
    @MockUser
    void updateCommunityBoard_success() throws Exception {
        //given
        CommunityBoardUpdateRequestDto requestDto = CommunityBoardUpdateRequestDto.builder()
                .title("XX아동센터 추천합니다.")
                .content("지난 주 토요일에 방문했는데 강추드려요.")
                .build();

        willDoNothing().given(updateCommunityBoardUseCase)
                .updateCommunityBoard(any(), any(), any(UUID.class));

        String requestBody = objectMapper.writeValueAsString(requestDto);

        //when
        mockMvc.perform(put("/api/community-board/{id}", 1)
                        .content(requestBody)
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", "Bearer access-token"))

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value("커뮤니티 게시글 수정 성공"));
    }

    @Test
    @DisplayName("커뮤니티 게시글 삭제 성공 테스트")
    @MockUser
    void deleteCommunityBoard_success() throws Exception {
        //given
        Long communityBoardId = 1L;
        willDoNothing().given(deleteCommunityBoardUseCase)
                .deleteCommunityBoard(any(UUID.class), any());

        //when
        mockMvc.perform(delete("/api/community-board/{id}", communityBoardId)
                        .header("Authorization", "Bearer access-token"))
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("커뮤니티 게시글 삭제 성공"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}
