package com.somemore.domains.review.service;

import com.somemore.domains.review.domain.Review;
import com.somemore.domains.review.dto.request.ReviewUpdateRequestDto;
import com.somemore.domains.review.repository.ReviewRepository;
import com.somemore.domains.review.service.validator.ReviewValidator;
import com.somemore.domains.review.usecase.UpdateReviewUseCase;
import com.somemore.global.exception.ExceptionMessage;
import com.somemore.global.exception.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Transactional
@Service
public class UpdateReviewService implements UpdateReviewUseCase {

    private final ReviewRepository reviewRepository;
    private final ReviewValidator reviewValidator;

    @Override
    public void updateReview(Long id, UUID volunteerId, ReviewUpdateRequestDto requestDto) {
        Review review = getReview(id);
        reviewValidator.validateAuthor(review, volunteerId);
        review.updateWith(requestDto);
    }

    @Override
    public void updateReviewImageUrl(Long id, UUID volunteerId, String imgUrl) {
        Review review = getReview(id);
        reviewValidator.validateAuthor(review, volunteerId);
        review.updateWith(imgUrl);
    }

    private Review getReview(Long id) {
        return reviewRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException(ExceptionMessage.NOT_EXISTS_REVIEW));
    }
}
