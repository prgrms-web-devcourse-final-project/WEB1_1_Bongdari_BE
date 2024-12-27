package com.somemore.domains.review.usecase;

import java.util.UUID;

public interface DeleteReviewUseCase {

    void deleteReview(UUID volunteerId, Long id);
}
