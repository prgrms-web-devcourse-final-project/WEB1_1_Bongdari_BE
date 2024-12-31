package com.somemore.domains.review.service;

import com.somemore.domains.review.domain.Review;
import com.somemore.domains.review.repository.ReviewRepository;
import com.somemore.domains.review.service.validator.ReviewValidator;
import com.somemore.domains.review.usecase.DeleteReviewUseCase;
import com.somemore.domains.review.usecase.ReviewQueryUseCase;
import com.somemore.global.exception.ExceptionMessage;
import com.somemore.global.exception.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Transactional
@Service
public class DeleteReviewService implements DeleteReviewUseCase {

    private final ReviewQueryUseCase reviewQueryUseCase;
    private final ReviewValidator reviewValidator;

    @Override
    public void deleteReview(UUID volunteerId, Long id) {
        Review review = reviewQueryUseCase.getById(id);

        reviewValidator.validateAuthor(review, volunteerId);
        review.markAsDeleted();
    }

}
