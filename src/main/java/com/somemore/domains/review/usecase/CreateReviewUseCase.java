package com.somemore.domains.review.usecase;

import com.somemore.domains.review.dto.request.ReviewCreateRequestDto;
import java.util.UUID;

public interface CreateReviewUseCase {

    Long createReview(ReviewCreateRequestDto requestDto, UUID volunteerId, String imgUrl);


}
