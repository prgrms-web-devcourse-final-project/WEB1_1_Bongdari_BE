package com.somemore.domains.review.service;

import com.somemore.domains.review.domain.Review;
import com.somemore.domains.review.dto.request.ReviewCreateRequestDto;
import com.somemore.domains.review.repository.ReviewRepository;
import com.somemore.domains.volunteerapply.domain.VolunteerApply;
import com.somemore.domains.volunteerapply.repository.VolunteerApplyRepository;
import com.somemore.global.exception.BadRequestException;
import com.somemore.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static com.somemore.domains.volunteerapply.domain.ApplyStatus.APPROVED;
import static com.somemore.global.exception.ExceptionMessage.REVIEW_ALREADY_EXISTS;
import static com.somemore.global.exception.ExceptionMessage.REVIEW_RESTRICTED_TO_ATTENDED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class CreateReviewServiceTest extends IntegrationTestSupport {

    @Autowired
    private CreateReviewService createReviewService;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private VolunteerApplyRepository volunteerApplyRepository;

    @DisplayName("리뷰 생성")
    @Test
    void createReview() {
        // given
        UUID volunteerId = UUID.randomUUID();
        Long recruitId = 200L;
        VolunteerApply apply = VolunteerApply.builder()
                .volunteerId(volunteerId)
                .recruitBoardId(200L)
                .status(APPROVED)
                .attended(true)
                .build();
        volunteerApplyRepository.save(apply);

        ReviewCreateRequestDto requestDto = ReviewCreateRequestDto.builder()
                .recruitBoardId(recruitId)
                .title("리뷰 제목")
                .content("리뷰 내용")
                .build();

        // when
        Long reviewId = createReviewService.createReview(requestDto, volunteerId, "");

        // then
        Optional<Review> findReview = reviewRepository.findById(reviewId);
        assertThat(findReview).isPresent();
        assertThat(findReview.get().getId()).isEqualTo(reviewId);
    }

    @DisplayName("참석하지 않은 봉사 활동에 대해 리뷰를 생성하면 에러가 발생한다")
    @Test
    void createReviewWhenNotCompleted() {
        // given
        UUID volunteerId = UUID.randomUUID();
        Long recruitId = 200L;
        VolunteerApply apply = VolunteerApply.builder()
                .volunteerId(volunteerId)
                .recruitBoardId(200L)
                .status(APPROVED)
                .attended(false)
                .build();
        volunteerApplyRepository.save(apply);

        ReviewCreateRequestDto requestDto = ReviewCreateRequestDto.builder()
                .recruitBoardId(recruitId)
                .title("리뷰 제목")
                .content("리뷰 내용")
                .build();

        // when
        // then
        assertThatThrownBy(
                () -> createReviewService.createReview(requestDto, volunteerId, "")
        ).isInstanceOf(BadRequestException.class)
                .hasMessage(REVIEW_RESTRICTED_TO_ATTENDED.getMessage());
    }

    @DisplayName("이미 작성한 봉사 활동에 대해 리뷰를 생성하면 에러가 발생한다")
    @Test
    void createReviewWhenExistsReview() {
        // given
        UUID volunteerId = UUID.randomUUID();
        Long recruitId = 200L;
        VolunteerApply apply = VolunteerApply.builder()
                .volunteerId(volunteerId)
                .recruitBoardId(200L)
                .status(APPROVED)
                .attended(false)
                .build();
        volunteerApplyRepository.save(apply);

        Review review = Review.builder()
                .volunteerApplyId(apply.getId())
                .volunteerId(volunteerId)
                .title("리뷰 제목")
                .content("리뷰 내용")
                .imgUrl("")
                .build();

        reviewRepository.save(review);

        ReviewCreateRequestDto requestDto = ReviewCreateRequestDto.builder()
                .recruitBoardId(recruitId)
                .title("리뷰 제목")
                .content("리뷰 내용")
                .build();

        // when
        // then
        assertThatThrownBy(
                () -> createReviewService.createReview(requestDto, volunteerId, "")
        ).isInstanceOf(BadRequestException.class)
                .hasMessage(REVIEW_ALREADY_EXISTS.getMessage());
    }

}
