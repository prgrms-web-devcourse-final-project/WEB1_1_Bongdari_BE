package com.somemore.community.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.somemore.ControllerTestSupport;
import com.somemore.WithMockCustomUser;
import com.somemore.community.dto.request.CommunityCommentCreateRequestDto;
import com.somemore.community.dto.request.CommunityCommentUpdateRequestDto;
import com.somemore.community.usecase.comment.CreateCommunityCommentUseCase;
import com.somemore.community.usecase.comment.DeleteCommunityCommentUseCase;
import com.somemore.community.usecase.comment.UpdateCommunityCommentUseCase;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

public class CommunityCommentCommandApiControllerTest extends ControllerTestSupport {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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

        MockMultipartFile requestData = new MockMultipartFile(
                "data",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(requestDto)
        );

        given(createCommunityCommentUseCase.createCommunityComment(any(), any(UUID.class),
                eq(communityBoardId))).willReturn(communityCommentId);

        //when
        mockMvc.perform(multipart("/api/community-board/{boardId}/comment", communityBoardId)
                        .file(requestData)
                        .contentType(MULTIPART_FORM_DATA)
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

        MockMultipartFile requestData = new MockMultipartFile(
                "data",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(requestDto)
        );

        willDoNothing().given(updateCommunityCommentUseCase)
                .updateCommunityComment(any(), any(),
                        any(UUID.class), any());

        MockMultipartHttpServletRequestBuilder builder =
                multipart("/api/community-board/{boardId}/comment/{id}", communityBoardId, communityCommentId);
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });

        //when
        mockMvc.perform(builder
                .file(requestData)
                .contentType(MULTIPART_FORM_DATA)
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
        willDoNothing().given(deleteCommunityCommentUseCase).deleteCommunityComment(any(UUID.class), any(), any());

        //when
        mockMvc.perform(delete("/api/community-board/{boardId}/comment/{id}", communityBoardId, communityCommentId)
                        .header("Authorization", "Bearer access-token"))
                //then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("커뮤니티 댓글 삭제 성공"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}
