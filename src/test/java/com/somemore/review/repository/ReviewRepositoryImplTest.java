package com.somemore.review.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.somemore.IntegrationTestSupport;
import com.somemore.review.domain.Review;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class ReviewRepositoryImplTest extends IntegrationTestSupport {

    @Autowired
    private ReviewRepositoryImpl reviewRepository;

    @DisplayName("리뷰 생성 및 조회")
    @Test
    void saveAndFind() {
        // given
        Review review = Review.builder()
                .volunteerApplyId(1L)
                .volunteerId(UUID.randomUUID())
                .title("리뷰 제목")
                .content("리뷰 내용")
                .imgUrl("")
                .build();
        reviewRepository.save(review);

        // when
        Optional<Review> findReview = reviewRepository.findById(review.getId());

        // then
        assertThat(findReview).isPresent();
        assertThat(findReview.get().getId()).isEqualTo(review.getId());
    }

    @DisplayName("봉사 지원 아이디로 리뷰 존재 유무를 확인할 수 있다")
    @Test
    void existsByVolunteerApplyId() {
        // given
        Long volunteerApplyId = 1L;
        Review review = Review.builder()
                .volunteerApplyId(volunteerApplyId)
                .volunteerId(UUID.randomUUID())
                .title("리뷰 제목")
                .content("리뷰 내용")
                .imgUrl("")
                .build();
        reviewRepository.save(review);

        // when
        boolean result = reviewRepository.existsByVolunteerApplyId(volunteerApplyId);

        // then
        assertThat(result).isTrue();
    }

}
