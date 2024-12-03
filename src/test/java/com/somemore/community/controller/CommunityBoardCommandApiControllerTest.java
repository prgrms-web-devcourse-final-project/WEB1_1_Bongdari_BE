package com.somemore.community.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
import com.somemore.community.dto.request.CommunityBoardCreateRequestDto;
import com.somemore.community.dto.request.CommunityBoardUpdateRequestDto;
import com.somemore.community.usecase.board.CreateCommunityBoardUseCase;
import com.somemore.community.usecase.board.DeleteCommunityBoardUseCase;
import com.somemore.community.usecase.board.UpdateCommunityBoardUseCase;
import com.somemore.imageupload.usecase.ImageUploadUseCase;
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

public class CommunityBoardCommandApiControllerTest extends ControllerTestSupport {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateCommunityBoardUseCase createCommunityBoardUseCase;

    @MockBean
    private UpdateCommunityBoardUseCase updateCommunityBoardUseCase;

    @MockBean
    private DeleteCommunityBoardUseCase deleteCommunityBoardUseCase;

    @MockBean
    private ImageUploadUseCase imageUploadUseCase;

    @Test
    @DisplayName("커뮤니티 게시글 등록 성공 테스트")
    @WithMockCustomUser
    void createCommunityBoard_success() throws Exception {
        //given
        CommunityBoardCreateRequestDto dto = CommunityBoardCreateRequestDto.builder()
                .title("11/29 OO도서관 봉사 같이 갈 사람 모집합니다.")
                .content("저 포함 5명이 같이 가면 좋을 거 같아요.")
                .build();

        MockMultipartFile imageFile = new MockMultipartFile(
                "img_file",
                "test-image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image content".getBytes()
        );

        MockMultipartFile requestData = new MockMultipartFile(
                "data",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(dto)
        );

        String mockImageUrl = "http://example.com/image/test-image.jpg";
        long communityBoardId = 1L;

        given(imageUploadUseCase.uploadImage(any())).willReturn(mockImageUrl);
        given(createCommunityBoardUseCase.createCommunityBoard(any(), any(UUID.class),
                anyString())).willReturn(communityBoardId);

        //when
        mockMvc.perform(multipart("/api/community-board")
                    .file(requestData)
                    .file(imageFile)
                    .contentType(MULTIPART_FORM_DATA)
                    .header("Authorization", "Bearer access-token"))
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.data").value(communityBoardId))
                .andExpect(jsonPath("$.message").value("커뮤니티 게시글 등록 성공"));
    }

    @Test
    @DisplayName("커뮤니티 게시글 수정 성공 테스트")
    @WithMockCustomUser
    void updateCommunityBoard_success() throws Exception {
        //given
        CommunityBoardUpdateRequestDto requestDto = CommunityBoardUpdateRequestDto.builder()
                .title("XX아동센터 추천합니다.")
                .content("지난 주 토요일에 방문했는데 강추드려요.")
                .build();

        MockMultipartFile imageFile = new MockMultipartFile(
                "img_file",
                "test-image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image content".getBytes()
        );

        MockMultipartFile requestData = new MockMultipartFile(
                "data",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(requestDto)
        );

        String imageUrl = "http://example.com/image/test-image.jpg";

        given(imageUploadUseCase.uploadImage(any())).willReturn(imageUrl);
        willDoNothing().given(updateCommunityBoardUseCase)
                .updateCommunityBoard(any(), any(), any(UUID.class), anyString());

        MockMultipartHttpServletRequestBuilder builder = multipart("/api/community-board/{id}", 1);
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
                .file(imageFile)
                .contentType(MULTIPART_FORM_DATA)
                .header("Authorization", "Bearer access-token"))

                //then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value("커뮤니티 게시글 수정 성공"));
    }

    @Test
    @DisplayName("커뮤니티 게시글 삭제 성공 테스트")
    @WithMockCustomUser
    void deleteCommunityBoard_success() throws Exception {
        //given
        Long communityBoardId = 1L;
        willDoNothing().given(deleteCommunityBoardUseCase).deleteCommunityBoard(any(UUID.class), any());

        //when
        mockMvc.perform(delete("/api/community-board/{id}", communityBoardId)
                .header("Authorization", "Bearer access-token"))
                //then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("커뮤니티 게시글 삭제 성공"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}
