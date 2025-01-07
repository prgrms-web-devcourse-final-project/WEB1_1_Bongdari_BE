package com.somemore.domains.review.service.validator;

import com.somemore.domains.review.domain.Review;
import com.somemore.global.exception.BadRequestException;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.somemore.global.exception.ExceptionMessage.UNAUTHORIZED_REVIEW;

@Component
public class ReviewValidator {

    public void validateWriter(Review review, UUID volunteerId) {
        if (review.isWriter(volunteerId)) {
            return;
        }

        throw new BadRequestException(UNAUTHORIZED_REVIEW);
    }
}
