package com.somemore.domains.review.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.somemore.domains.review.dto.request.ReviewCreateRequestDto;
import com.somemore.domains.review.usecase.CreateReviewUseCase;
import com.somemore.global.imageupload.usecase.ImageUploadUseCase;
import com.somemore.support.ControllerTestSupport;
import com.somemore.support.annotation.WithMockCustomUser;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

class ReviewCommandApiControllerTest extends ControllerTestSupport {

    @MockBean
    private ImageUploadUseCase imageUploadUseCase;

    @MockBean
    private CreateReviewUseCase createReviewUseCase;

    @DisplayName("리뷰 생성 성공")
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

        MockMultipartFile requestData = getRequestData(requestDto);

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

    @DisplayName("리뷰 생성 유효성 테스트 - 모집글 아이디")
    @Test
    @WithMockCustomUser()
    void createReviewValidateTestRecruitBoardId() throws Exception {
        // given
        ReviewCreateRequestDto requestDto = ReviewCreateRequestDto.builder()
                .title("리뷰 제목")
                .content("리뷰 내용")
                .build();

        MockMultipartFile requestData = getRequestData(requestDto);

        given(imageUploadUseCase.uploadImage(any())).willReturn("");

        // when
        mockMvc.perform(multipart("/api/review")
                        .file(requestData)
                        .contentType(MULTIPART_FORM_DATA)
                        .header("Authorization", "Bearer access-token"))
                // then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.title").value("유효성 예외"))
                .andExpect(jsonPath("$.detail").value("봉사 모집글 아이디는 필수 값입니다."));
    }

    @DisplayName("리뷰 생성 유효성 테스트 - 제목")
    @Test
    @WithMockCustomUser()
    void createReviewValidateTestTitle() throws Exception {
        // given
        ReviewCreateRequestDto requestDto = ReviewCreateRequestDto.builder()
                .recruitBoardId(1L)
                .content("리뷰 내용")
                .build();

        MockMultipartFile requestData = getRequestData(requestDto);

        given(imageUploadUseCase.uploadImage(any())).willReturn("");

        // when
        mockMvc.perform(multipart("/api/review")
                        .file(requestData)
                        .contentType(MULTIPART_FORM_DATA)
                        .header("Authorization", "Bearer access-token"))
                // then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.title").value("유효성 예외"))
                .andExpect(jsonPath("$.detail").value("리뷰 제목은 필수 값입니다."));
    }

    @DisplayName("리뷰 생성 유효성 테스트 - 내용")
    @Test
    @WithMockCustomUser()
    void createReviewValidateTestContent() throws Exception {
        // given
        ReviewCreateRequestDto requestDto = ReviewCreateRequestDto.builder()
                .recruitBoardId(1L)
                .title("리뷰 제목")
                .build();

        MockMultipartFile requestData = getRequestData(requestDto);

        given(imageUploadUseCase.uploadImage(any())).willReturn("");

        // when
        mockMvc.perform(multipart("/api/review")
                        .file(requestData)
                        .contentType(MULTIPART_FORM_DATA)
                        .header("Authorization", "Bearer access-token"))
                // then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.title").value("유효성 예외"))
                .andExpect(jsonPath("$.detail").value("리뷰 내용은 필수 값입니다."));
    }

    private MockMultipartFile getRequestData(ReviewCreateRequestDto requestDto)
            throws JsonProcessingException {
        return new MockMultipartFile(
                "data",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(requestDto)
        );
    }
}
