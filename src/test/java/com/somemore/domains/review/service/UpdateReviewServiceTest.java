package com.somemore.domains.review.service;

import com.somemore.domains.review.domain.Review;
import com.somemore.domains.review.dto.request.ReviewUpdateRequestDto;
import com.somemore.domains.review.repository.ReviewRepository;
import com.somemore.global.exception.NoSuchElementException;
import com.somemore.support.IntegrationTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_REVIEW;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class UpdateReviewServiceTest extends IntegrationTestSupport {

    @Autowired
    private UpdateReviewService updateReviewService;

    @Autowired
    private ReviewRepository reviewRepository;

    private UUID volunteerId;
    private Review review;

    @BeforeEach
    void setUp() {
        volunteerId = UUID.randomUUID();
        review = createReview(1L, volunteerId);
        reviewRepository.save(review);
    }

    @DisplayName("리뷰 정보를 업데이트 할 수 있다.")
    @Test
    void updateReview() {
        // given
        Long id = review.getId();
        ReviewUpdateRequestDto dto = ReviewUpdateRequestDto.builder()
                .title("업데이트 제목")
                .content("업데이트 내용")
                .build();

        // when
        updateReviewService.updateReview(id, volunteerId, dto);

        // then
        Review updateReview = reviewRepository.findById(id).orElseThrow();
        assertThat(updateReview.getTitle()).isEqualTo(dto.title());
        assertThat(updateReview.getContent()).isEqualTo(dto.content());
    }

    @DisplayName("존재하지 않는 리뷰를 업데이트할 경우 에러가 발생한다.")
    @Test
    void updateReviewWhenDoesNotExistId() {
        // given
        Long wrongId = 999L;

        ReviewUpdateRequestDto dto = ReviewUpdateRequestDto.builder()
                .title("업데이트 제목")
                .content("업데이트 내용")
                .build();

        // when
        // then
        assertThatThrownBy(
                () -> updateReviewService.updateReview(wrongId, volunteerId, dto))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage(NOT_EXISTS_REVIEW.getMessage());
    }

    @DisplayName("리뷰 이미지 링크를 업데이트 할 수 있다.")
    @Test
    void updateReviewImageUrl() {
        // given
        Long id = review.getId();
        String newImgUrl = "newLink.co.kr";

        // when
        updateReviewService.updateReviewImageUrl(id, volunteerId, newImgUrl);

        // then
        Review updateReview = reviewRepository.findById(id).orElseThrow();
        assertThat(updateReview.getImgUrl()).isEqualTo(newImgUrl);
    }

    @DisplayName("존재하지 않는 리뷰의 이미지링크를 업데이트할 경우 에러가 발생한다.")
    @Test
    void updateReviewImageUrlWhenDoesNotExistId() {
        // given
        Long wrongId = 999L;
        String newImgUrl = "newLink.co.kr";

        // when
        // then
        assertThatThrownBy(
                () -> updateReviewService.updateReviewImageUrl(wrongId, volunteerId, newImgUrl))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage(NOT_EXISTS_REVIEW.getMessage());
    }

    private Review createReview(Long applyId, UUID volunteerId) {
        return Review.builder()
                .volunteerApplyId(applyId)
                .volunteerId(volunteerId)
                .title("제목제목")
                .content("내용내용")
                .imgUrl("이미지링크")
                .build();
    }
}
