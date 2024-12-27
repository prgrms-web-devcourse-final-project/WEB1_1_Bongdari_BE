package com.somemore.domains.review.service.validator;

import com.somemore.domains.review.domain.Review;
import com.somemore.global.exception.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.somemore.global.exception.ExceptionMessage.UNAUTHORIZED_REVIEW;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReviewValidatorTest {

    private final ReviewValidator reviewValidator = new ReviewValidator();

    @DisplayName("리뷰 작성자인지 검증할 수 있다")
    @Test
    void validateAuthor() {
        // given
        UUID volunteerId = UUID.randomUUID();
        Review review = createReview(volunteerId, "리뷰 제목", "내용내용", "이미지링크");

        // when
        // then
        assertThatCode(
                () -> reviewValidator.validateAuthor(review, volunteerId))
                .doesNotThrowAnyException();
    }

    @DisplayName("리뷰 작성자와 일치 하지 않는 경우 에러가 발생한다.")
    @Test
    void validateAuthorWhenMissMatch() {
        // given
        UUID wrongVolunteerId = UUID.randomUUID();
        Review review = createReview(UUID.randomUUID(), "다른 제목", "다른 내용", "다른 링크");

        // when
        // then
        assertThatThrownBy(
                () -> reviewValidator.validateAuthor(review, wrongVolunteerId))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(UNAUTHORIZED_REVIEW.getMessage());
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
