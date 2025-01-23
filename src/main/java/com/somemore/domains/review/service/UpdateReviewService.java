package com.somemore.domains.review.service;

import com.somemore.domains.review.domain.Review;
import com.somemore.domains.review.dto.request.ReviewUpdateRequestDto;
import com.somemore.domains.review.service.validator.ReviewValidator;
import com.somemore.domains.review.usecase.ReviewQueryUseCase;
import com.somemore.domains.review.usecase.UpdateReviewUseCase;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class UpdateReviewService implements UpdateReviewUseCase {

    private final ReviewQueryUseCase reviewQueryUseCase;
    private final ReviewValidator reviewValidator;

    @Override
    public void updateReview(Long id, UUID volunteerId, ReviewUpdateRequestDto requestDto) {
        Review review = reviewQueryUseCase.getById(id);
        reviewValidator.validateWriter(review, volunteerId);
        review.updateWith(requestDto);
    }

}
