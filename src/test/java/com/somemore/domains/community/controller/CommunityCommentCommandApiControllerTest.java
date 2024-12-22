package com.somemore.domains.community.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.somemore.domains.community.dto.request.CommunityCommentCreateRequestDto;
import com.somemore.domains.community.dto.request.CommunityCommentUpdateRequestDto;
import com.somemore.domains.community.usecase.comment.CreateCommunityCommentUseCase;
import com.somemore.domains.community.usecase.comment.DeleteCommunityCommentUseCase;
import com.somemore.domains.community.usecase.comment.UpdateCommunityCommentUseCase;
import com.somemore.support.ControllerTestSupport;
import com.somemore.support.annotation.WithMockCustomUser;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

public class CommunityCommentCommandApiControllerTest extends ControllerTestSupport {

    @MockBean
    private CreateCommunityCommentUseCase createCommunityCommentUseCase;

    @MockBean
    private UpdateCommunityCommentUseCase updateCommunityCommentUseCase;

    @MockBean
    private DeleteCommunityCommentUseCase deleteCommunityCommentUseCase;

    private final long communityBoardId = 1L;
    private final long communityCommentId = 1L;

    @Test
    @DisplayName("커뮤니티 댓글 등록 성공 테스트")
    @WithMockCustomUser
    void createCommunityComment() throws Exception {
        //given
        CommunityCommentCreateRequestDto requestDto = CommunityCommentCreateRequestDto.builder()
                .content("몇시에 하는지 알 수 있을까요?")
                .parentCommentId(null)
                .build();

        given(createCommunityCommentUseCase.createCommunityComment(any(), any(UUID.class),
                eq(communityBoardId))).willReturn(communityCommentId);

        //when
        mockMvc.perform(post("/api/community-board/{boardId}/comment", communityBoardId)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer access-token"))

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.data").value(communityCommentId))
                .andExpect(jsonPath("$.message").value("커뮤니티 댓글 등록 성공"));
    }

    @Test
    @DisplayName("커뮤니티 댓글 수정 성공 테스트")
    @WithMockCustomUser
    void updateCommunityComment_success() throws Exception {
        //given
        CommunityCommentUpdateRequestDto requestDto = CommunityCommentUpdateRequestDto.builder()
                .content("감사합니다.")
                .build();

        willDoNothing().given(updateCommunityCommentUseCase)
                .updateCommunityComment(any(), any(), any(UUID.class), any());

        //when
        mockMvc.perform(put("/api/community-board/{boardId}/comment/{id}", communityBoardId,
                        communityCommentId)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer access-token"))

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value("커뮤니티 댓글 수정 성공"));
    }

    @Test
    @DisplayName("커뮤니티 댓글 삭제 성공 테스트")
    @WithMockCustomUser
    void deleteCommunityComment_success() throws Exception {
        //given
        willDoNothing().given(deleteCommunityCommentUseCase)
                .deleteCommunityComment(any(UUID.class), any(), any());

        //when
        mockMvc.perform(delete("/api/community-board/{boardId}/comment/{id}", communityBoardId,
                        communityCommentId)
                        .header("Authorization", "Bearer access-token"))
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("커뮤니티 댓글 삭제 성공"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}
