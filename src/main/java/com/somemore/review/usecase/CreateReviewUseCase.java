package com.somemore.review.usecase;

import com.somemore.review.dto.request.ReviewCreateRequestDto;
import java.util.UUID;

public interface CreateReviewUseCase {

    Long createReview(ReviewCreateRequestDto requestDto, UUID volunteerId, String imgUrl);


}
