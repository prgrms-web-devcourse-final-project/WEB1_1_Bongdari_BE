package com.somemore.domains.review.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.somemore.domains.review.domain.Review;
import com.somemore.domains.review.dto.request.ReviewUpdateRequestDto;
import com.somemore.domains.review.repository.ReviewRepository;
import com.somemore.support.IntegrationTestSupport;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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
