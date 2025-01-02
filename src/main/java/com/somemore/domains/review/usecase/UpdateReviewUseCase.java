package com.somemore.domains.review.usecase;

import com.somemore.domains.review.dto.request.ReviewUpdateRequestDto;

import java.util.UUID;

public interface UpdateReviewUseCase {

    void updateReview(Long id, UUID volunteerId, ReviewUpdateRequestDto requestDto);

    void updateReviewImageUrl(Long id, UUID volunteerId, String imgUrl);
}
