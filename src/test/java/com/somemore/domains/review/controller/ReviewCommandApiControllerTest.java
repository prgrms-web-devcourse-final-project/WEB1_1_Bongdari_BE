package com.somemore.domains.review.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.somemore.domains.review.dto.request.ReviewCreateRequestDto;
import com.somemore.domains.review.dto.request.ReviewUpdateRequestDto;
import com.somemore.domains.review.usecase.CreateReviewUseCase;
import com.somemore.domains.review.usecase.DeleteReviewUseCase;
import com.somemore.domains.review.usecase.UpdateReviewUseCase;
import com.somemore.support.ControllerTestSupport;
import com.somemore.support.annotation.MockUser;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

class ReviewCommandApiControllerTest extends ControllerTestSupport {

    @MockBean
    private CreateReviewUseCase createReviewUseCase;

    @MockBean
    private UpdateReviewUseCase updateReviewUseCase;

    @MockBean
    private DeleteReviewUseCase deleteReviewUseCase;

    @DisplayName("리뷰 생성 성공")
    @Test
    @MockUser
    void createReview() throws Exception {
        // given
        ReviewCreateRequestDto requestDto = ReviewCreateRequestDto.builder()
                .volunteerApplyId(1L)
                .title("리뷰 제목")
                .content("리뷰 내용")
                .build();

        String requestData = objectMapper.writeValueAsString(requestDto);

        Long reviewId = 1L;

        given(createReviewUseCase.createReview(any(), any(UUID.class)))
                .willReturn(reviewId);

        // when
        mockMvc.perform(post("/api/review")
                        .content(requestData)
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", "Bearer access-token"))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.data").value(reviewId))
                .andExpect(jsonPath("$.message").value("리뷰 등록 성공"));
    }

    @DisplayName("리뷰 생성 유효성 테스트 - 모집글 아이디")
    @Test
    @MockUser
    void createReviewValidateTestRecruitBoardId() throws Exception {
        // given
        ReviewCreateRequestDto requestDto = ReviewCreateRequestDto.builder()
                .title("리뷰 제목")
                .content("리뷰 내용")
                .build();

        String requestData = objectMapper.writeValueAsString(requestDto);

        // when
        mockMvc.perform(post("/api/review")
                        .content(requestData)
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", "Bearer access-token"))
                // then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.title").value("유효성 예외"))
                .andExpect(jsonPath("$.detail").value("봉사 지원 아이디는 필수 값입니다."));
    }

    @DisplayName("리뷰 생성 유효성 테스트 - 제목")
    @Test
    @MockUser
    void createReviewValidateTestTitle() throws Exception {
        // given
        ReviewCreateRequestDto requestDto = ReviewCreateRequestDto.builder()
                .volunteerApplyId(1L)
                .content("리뷰 내용")
                .build();

        String requestData = objectMapper.writeValueAsString(requestDto);

        // when
        mockMvc.perform(post("/api/review")
                        .content(requestData)
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", "Bearer access-token"))
                // then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.title").value("유효성 예외"))
                .andExpect(jsonPath("$.detail").value("리뷰 제목은 필수 값입니다."));
    }

    @DisplayName("리뷰 생성 유효성 테스트 - 내용")
    @Test
    @MockUser
    void createReviewValidateTestContent() throws Exception {
        // given
        ReviewCreateRequestDto requestDto = ReviewCreateRequestDto.builder()
                .volunteerApplyId(1L)
                .title("리뷰 제목")
                .build();

        String requestData = objectMapper.writeValueAsString(requestDto);

        // when
        mockMvc.perform(post("/api/review")
                        .content(requestData)
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", "Bearer access-token"))
                // then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.title").value("유효성 예외"))
                .andExpect(jsonPath("$.detail").value("리뷰 내용은 필수 값입니다."));
    }

    @DisplayName("리뷰 수정 성공")
    @Test
    @MockUser
    void updateReview() throws Exception {
        // given
        ReviewUpdateRequestDto requestDto = ReviewUpdateRequestDto.builder()
                .title("리뷰 수정 제목")
                .content("리뷰 수정 내용")
                .build();

        Long reviewId = 1L;

        willDoNothing().given(updateReviewUseCase).updateReview(any(), any(UUID.class), any());
        String requestBody = objectMapper.writeValueAsString(requestDto);

        // when
        mockMvc.perform(put("/api/review/{id}", reviewId)
                        .content(requestBody)
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", "Bearer access-token"))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("리뷰 수정 성공"));
    }

    @DisplayName("리뷰 수정 유효성 테스트 - 제목")
    @Test
    @MockUser
    void updateReviewValidateTitle() throws Exception {
        // given
        ReviewUpdateRequestDto requestDto = ReviewUpdateRequestDto.builder()
                .content("업데이트 내용")
                .build();

        Long reviewId = 1L;
        String requestBody = objectMapper.writeValueAsString(requestDto);

        // when
        mockMvc.perform(put("/api/review/{id}", reviewId)
                        .content(requestBody)
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", "Bearer access-token"))
                // then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.title").value("유효성 예외"))
                .andExpect(jsonPath("$.detail").value("리뷰 제목은 필수 값입니다."));
    }

    @DisplayName("리뷰 수정 유효성 테스트 - 내용")
    @Test
    @MockUser
    void updateReviewValidateContent() throws Exception {
        // given
        ReviewUpdateRequestDto requestDto = ReviewUpdateRequestDto.builder()
                .title("업데이트 제목")
                .build();

        Long reviewId = 1L;
        String requestBody = objectMapper.writeValueAsString(requestDto);

        // when
        mockMvc.perform(put("/api/review/{id}", reviewId)
                        .content(requestBody)
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", "Bearer access-token"))
                // then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.title").value("유효성 예외"))
                .andExpect(jsonPath("$.detail").value("리뷰 내용은 필수 값입니다."));
    }

    @DisplayName("리뷰 삭제 성공 테스트")
    @Test
    @MockUser
    void deleteReview() throws Exception {
        // given
        Long id = 1L;
        UUID volunteerId = UUID.randomUUID();

        willDoNothing().given(deleteReviewUseCase).deleteReview(volunteerId, id);

        // when
        mockMvc.perform(delete("/api/review/{id}", id)
                        .header("Authorization", "Bearer access-token"))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("리뷰 삭제 성공"));
    }

}
