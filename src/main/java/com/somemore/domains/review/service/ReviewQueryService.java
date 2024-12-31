package com.somemore.domains.review.service;

import com.somemore.domains.center.usecase.query.CenterQueryUseCase;
import com.somemore.domains.review.domain.Review;
import com.somemore.domains.review.dto.condition.ReviewSearchCondition;
import com.somemore.domains.review.dto.response.ReviewDetailResponseDto;
import com.somemore.domains.review.dto.response.ReviewDetailWithNicknameResponseDto;
import com.somemore.domains.review.repository.ReviewRepository;
import com.somemore.domains.review.usecase.ReviewQueryUseCase;
import com.somemore.domains.volunteer.domain.Volunteer;
import com.somemore.domains.volunteer.usecase.VolunteerQueryUseCase;
import com.somemore.global.exception.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_REVIEW;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReviewQueryService implements ReviewQueryUseCase {

    private final ReviewRepository reviewRepository;
    private final VolunteerQueryUseCase volunteerQueryUseCase;
    private final CenterQueryUseCase centerQueryUseCase;

    @Override
    public Review getById(Long id) {
        return reviewRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException(NOT_EXISTS_REVIEW));
    }

    @Override
    public ReviewDetailResponseDto getDetailById(Long id) {
        Review review = getById(id);
        return ReviewDetailResponseDto.from(review);
    }

    @Override
    public Page<ReviewDetailWithNicknameResponseDto> getDetailsWithNicknameByVolunteerId(
            UUID volunteerId,
            ReviewSearchCondition condition
    ) {
        String nickname = volunteerQueryUseCase.getNicknameById(volunteerId);
        Page<Review> reviews = reviewRepository.findAllByVolunteerIdAndSearch(volunteerId, condition);

        return reviews.map(
                review -> ReviewDetailWithNicknameResponseDto.from(review, nickname)
        );
    }

    @Override
    public Page<ReviewDetailWithNicknameResponseDto> getDetailsWithNicknameByCenterId(
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
                    return ReviewDetailWithNicknameResponseDto.from(review, nickname);
                });
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
