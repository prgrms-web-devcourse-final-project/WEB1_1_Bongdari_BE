package com.somemore.domains.review.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.somemore.domains.review.domain.Review;
import com.somemore.domains.review.repository.ReviewRepository;
import com.somemore.support.IntegrationTestSupport;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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

    private Review createReview(UUID volunteerId, String title, String content, String imgUrl) {
        return Review.builder()
                .volunteerApplyId(1L)
                .volunteerId(volunteerId)
                .title(title)
                .content(content)
                .build();
    }
}
