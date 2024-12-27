package com.somemore.domains.review.service;

import com.somemore.domains.review.domain.Review;
import com.somemore.domains.review.repository.ReviewRepository;
import com.somemore.global.exception.NoSuchElementException;
import com.somemore.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_REVIEW;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class DeleteReviewServiceTest extends IntegrationTestSupport {

    @Autowired
    private DeleteReviewService deleteReviewService;

    @Autowired
    private ReviewRepository reviewRepository;

    @DisplayName("리뷰 아이디와 작성자 아이디로 리뷰를 삭제할 수 있다.")
    @Test
    void deleteReview() {
        // given
        UUID volunteerId = UUID.randomUUID();
        String title = "리뷰 제목";
        String content = "리뷰 내용";
        String imgUrl = "리뷰 이미지";

        Review review = createReview(volunteerId, title, content, imgUrl);
        reviewRepository.save(review);

        // when
        deleteReviewService.deleteReview(volunteerId, review.getId());

        // then
        Optional<Review> findReview = reviewRepository.findById(review.getId());
        assertThat(findReview).isEmpty();
    }

    @DisplayName("존재하지 않는 리뷰를 삭제하려고 하는 경우 에러가 발생한다")
    @Test
    void deleteReviewWhenDoesNotExist() {
        // given
        UUID volunteerId = UUID.randomUUID();
        Long wrongId = 999L;

        // when
        // then
        assertThatThrownBy(
                () -> deleteReviewService.deleteReview(volunteerId, wrongId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage(NOT_EXISTS_REVIEW.getMessage());
    }

    private Review createReview(UUID volunteerId, String title, String content, String imgUrl) {
        return Review.builder()
                .volunteerApplyId(1L)
                .volunteerId(volunteerId)
                .title(title)
                .content(content)
                .imgUrl(imgUrl)
                .build();
    }
}
