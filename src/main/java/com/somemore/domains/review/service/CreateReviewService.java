package com.somemore.domains.review.service;

import static com.somemore.global.exception.ExceptionMessage.REVIEW_ALREADY_EXISTS;
import static com.somemore.global.exception.ExceptionMessage.REVIEW_RESTRICTED_TO_ATTENDED;

import com.somemore.domains.review.domain.Review;
import com.somemore.domains.review.dto.request.ReviewCreateRequestDto;
import com.somemore.domains.review.repository.ReviewRepository;
import com.somemore.domains.review.usecase.CreateReviewUseCase;
import com.somemore.domains.review.usecase.ReviewQueryUseCase;
import com.somemore.domains.volunteerapply.domain.VolunteerApply;
import com.somemore.domains.volunteerapply.usecase.VolunteerApplyQueryUseCase;
import com.somemore.global.exception.BadRequestException;
import com.somemore.global.exception.DuplicateException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class CreateReviewService implements CreateReviewUseCase {

    private final ReviewRepository reviewRepository;
    private final ReviewQueryUseCase reviewQueryUseCase;
    private final VolunteerApplyQueryUseCase volunteerApplyQueryUseCase;

    @Override
    public Long createReview(ReviewCreateRequestDto requestDto, UUID volunteerId) {
        validateDuplicateReview(requestDto.volunteerApplyId());

        VolunteerApply apply = volunteerApplyQueryUseCase.getById(requestDto.volunteerApplyId());
        validateActivityCompletion(apply);

        Review review = requestDto.toEntity(volunteerId);
        reviewRepository.save(review);

        return review.getId();
    }

    private void validateDuplicateReview(Long volunteerApplyId) {
        if (reviewQueryUseCase.existsByVolunteerApplyId(volunteerApplyId)) {
            throw new DuplicateException(REVIEW_ALREADY_EXISTS);
        }
    }

    private void validateActivityCompletion(VolunteerApply apply) {
        if (apply.isVolunteerActivityCompleted()) {
            return;
        }
        throw new BadRequestException(REVIEW_RESTRICTED_TO_ATTENDED);
    }
}
