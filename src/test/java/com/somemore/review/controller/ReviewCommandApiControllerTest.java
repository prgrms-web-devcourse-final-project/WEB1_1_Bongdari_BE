package com.somemore.review.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.somemore.ControllerTestSupport;
import com.somemore.WithMockCustomUser;
import com.somemore.global.imageupload.usecase.ImageUploadUseCase;
import com.somemore.review.dto.request.ReviewCreateRequestDto;
import com.somemore.review.usecase.CreateReviewUseCase;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

class ReviewCommandApiControllerTest extends ControllerTestSupport {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ImageUploadUseCase imageUploadUseCase;

    @MockBean
    private CreateReviewUseCase createReviewUseCase;

    @DisplayName("리뷰 생성")
    @Test
    @WithMockCustomUser()
    void createReview() throws Exception {
        // given
        ReviewCreateRequestDto requestDto = ReviewCreateRequestDto.builder()
                .recruitBoardId(1L)
                .title("리뷰 제목")
                .content("리뷰 내용")
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

        String imgUrl = "https://example.com/image/test-image.jpg";
        Long reviewId = 1L;

        given(imageUploadUseCase.uploadImage(any())).willReturn(imgUrl);
        given(createReviewUseCase.createReview(any(), any(UUID.class),
                anyString())).willReturn(reviewId);

        // when
        mockMvc.perform(multipart("/api/review")
                        .file(requestData)
                        .file(imageFile)
                        .contentType(MULTIPART_FORM_DATA)
                        .header("Authorization", "Bearer access-token"))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.data").value(reviewId))
                .andExpect(jsonPath("$.message").value("리뷰 등록 성공"));
    }
}
