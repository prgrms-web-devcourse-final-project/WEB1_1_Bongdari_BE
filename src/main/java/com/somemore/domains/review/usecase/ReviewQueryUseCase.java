package com.somemore.domains.review.usecase;

import com.somemore.domains.review.domain.Review;
import com.somemore.domains.review.dto.condition.ReviewSearchCondition;
import com.somemore.domains.review.dto.response.ReviewDetailResponseDto;
import com.somemore.domains.review.dto.response.ReviewDetailWithNicknameResponseDto;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface ReviewQueryUseCase {

    boolean existsByVolunteerApplyId(Long volunteerApplyId);

    Review getById(Long id);

    ReviewDetailResponseDto getDetailById(Long id);

    Page<ReviewDetailWithNicknameResponseDto> getDetailsWithNicknameByVolunteerId(UUID volunteerId, ReviewSearchCondition condition);

    Page<ReviewDetailWithNicknameResponseDto> getDetailsWithNicknameByCenterId(UUID centerId, ReviewSearchCondition condition);

}
