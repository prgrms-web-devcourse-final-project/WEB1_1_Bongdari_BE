package com.somemore.domains.review.domain;

import com.somemore.domains.review.dto.request.ReviewUpdateRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


class ReviewTest {

    private UUID volunteerId;
    private Review review;

    @BeforeEach
    void setUp() {
        volunteerId = UUID.randomUUID();
        review = createReview(1L, volunteerId);
    }

    @DisplayName("작성자 인지 확인할 수 있다.")
    @Test
    void isAuthor() {
        // given

        // when
        boolean result = review.isAuthor(volunteerId);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("리뷰 제목 내용을 업데이트 할 수 있다.")
    @Test
    void updateWith() {
        // given
        ReviewUpdateRequestDto dto = ReviewUpdateRequestDto.builder()
                .title("업데이트 제목")
                .content("업데이트 내용")
                .build();

        // when
        review.updateWith(dto);

        // then
        assertThat(review.getTitle()).isEqualTo(dto.title());
        assertThat(review.getContent()).isEqualTo(dto.content());
    }

    @DisplayName("이미지 링크를 업데이트 할 수 있다.")
    @Test
    void updateWithImgUrl() {
        // given
        String newImgUrl = "newLink";

        // when
        review.updateWith(newImgUrl);

        // then
        assertThat(review.getImgUrl()).isEqualTo(newImgUrl);
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
