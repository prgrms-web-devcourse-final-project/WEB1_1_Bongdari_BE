package com.somemore.review.service;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_REVIEW;

import com.somemore.center.usecase.query.CenterQueryUseCase;
import com.somemore.global.exception.BadRequestException;
import com.somemore.review.domain.Review;
import com.somemore.review.dto.condition.ReviewSearchCondition;
import com.somemore.review.dto.response.ReviewResponseDto;
import com.somemore.review.dto.response.ReviewWithNicknameResponseDto;
import com.somemore.review.repository.ReviewRepository;
import com.somemore.review.usecase.ReviewQueryUseCase;
import com.somemore.volunteer.domain.Volunteer;
import com.somemore.volunteer.usecase.VolunteerQueryUseCase;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReviewQueryService implements ReviewQueryUseCase {

    private final ReviewRepository reviewRepository;
    private final VolunteerQueryUseCase volunteerQueryUseCase;
    private final CenterQueryUseCase centerQueryUseCase;

    @Override
    public Review getById(Long id) {
        return getReview(id);
    }

    @Override
    public ReviewResponseDto getReviewById(Long id) {
        Review review = getReview(id);
        return ReviewResponseDto.from(review);
    }

    @Override
    public Page<ReviewWithNicknameResponseDto> getReviewsByVolunteerId(
            UUID volunteerId,
            ReviewSearchCondition condition
    ) {
        String nickname = volunteerQueryUseCase.getNicknameById(volunteerId);
        Page<Review> reviews = reviewRepository.findAllByVolunteerIdAndSearch(volunteerId,
                condition);

        return reviews.map(
                review -> ReviewWithNicknameResponseDto.from(review, nickname)
        );
    }

    @Override
    public Page<ReviewWithNicknameResponseDto> getReviewsByCenterId(
            UUID centerId,
            ReviewSearchCondition condition
    ) {
        centerQueryUseCase.validateCenterExists(centerId);

        Page<Review> reviews = reviewRepository.findAllByCenterIdAndSearch(centerId, condition);
        List<UUID> volunteerIds = reviews.get().map(Review::getVolunteerId).toList();
        Map<UUID, String> volunteerNicknames = getVolunteerNicknames(volunteerIds);

        return reviews.map(
                review -> {
                    String nickname = volunteerNicknames.getOrDefault(review.getVolunteerId(),
                            "삭제된 아이디");
                    return ReviewWithNicknameResponseDto.from(review, nickname);
                });
    }

    private Review getReview(Long id) {
        return reviewRepository.findById(id).orElseThrow(
                () -> new BadRequestException(NOT_EXISTS_REVIEW)
        );
    }

    private Map<UUID, String> getVolunteerNicknames(List<UUID> volunteerIds) {
        List<Volunteer> volunteers = volunteerQueryUseCase.getAllByIds(volunteerIds);

        Map<UUID, String> volunteerNicknames = new HashMap<>();
        for (Volunteer volunteer : volunteers) {
            volunteerNicknames.put(volunteer.getId(), volunteer.getNickname());
        }

        return volunteerNicknames;
    }
}