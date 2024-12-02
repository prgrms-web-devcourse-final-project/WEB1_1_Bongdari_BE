package com.somemore.review.usecase;

import com.somemore.review.domain.Review;
import com.somemore.review.dto.condition.ReviewSearchCondition;
import com.somemore.review.dto.response.ReviewResponseDto;
import com.somemore.review.dto.response.ReviewWithNicknameResponseDto;
import java.util.UUID;
import org.springframework.data.domain.Page;

public interface ReviewQueryUseCase {

    Review getById(Long id);

    ReviewResponseDto getReviewById(Long id);

    Page<ReviewWithNicknameResponseDto> getReviewsByVolunteerId(UUID volunteerId,
            ReviewSearchCondition condition);

    Page<ReviewWithNicknameResponseDto> getReviewsByCenterId(UUID centerId,
            ReviewSearchCondition condition);

}
