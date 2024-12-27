package com.somemore.domains.review.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


class ReviewTest {

    @DisplayName("작성자 인지 확인할 수 있다.")
    @Test
    void isAuthor() {
        // given
        Long applyId = 1L;
        UUID volunteerId = UUID.randomUUID();
        String title = "리뷰 제목";

        Review review = createReview(applyId, volunteerId, title);

        // when
        boolean result = review.isAuthor(volunteerId);

        // then
        assertThat(result).isTrue();
    }

    private Review createReview(Long applyId, UUID volunteerId, String title) {
        return Review.builder()
                .volunteerApplyId(applyId)
                .volunteerId(volunteerId)
                .title(title)
                .content("내용내용")
                .imgUrl("이미지링크")
                .build();
    }
}
