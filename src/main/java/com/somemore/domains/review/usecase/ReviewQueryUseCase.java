package com.somemore.domains.review.usecase;

import com.somemore.domains.review.domain.Review;
import com.somemore.domains.review.dto.condition.ReviewSearchCondition;
import com.somemore.domains.review.dto.response.ReviewResponseDto;
import com.somemore.domains.review.dto.response.ReviewWithNicknameResponseDto;
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
