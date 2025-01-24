package com.somemore.domains.review.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.somemore.domains.review.dto.request.ReviewUpdateRequestDto;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
    void isWriter() {
        // given

        // when
        boolean result = review.isWriter(volunteerId);

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

    private Review createReview(Long applyId, UUID volunteerId) {
        return Review.builder()
                .volunteerApplyId(applyId)
                .volunteerId(volunteerId)
                .title("제목제목")
                .content("내용내용")
                .build();
    }
}
