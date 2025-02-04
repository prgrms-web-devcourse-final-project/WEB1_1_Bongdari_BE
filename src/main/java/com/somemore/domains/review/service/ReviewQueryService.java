package com.somemore.domains.review.service;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_REVIEW;

import com.somemore.domains.review.domain.Review;
import com.somemore.domains.review.dto.condition.ReviewSearchCondition;
import com.somemore.domains.review.dto.response.ReviewDetailResponseDto;
import com.somemore.domains.review.dto.response.ReviewDetailWithNicknameResponseDto;
import com.somemore.domains.review.repository.ReviewRepository;
import com.somemore.domains.review.usecase.ReviewQueryUseCase;
import com.somemore.domains.volunteerapply.usecase.VolunteerApplyQueryUseCase;
import com.somemore.global.exception.NoSuchElementException;
import com.somemore.volunteer.repository.record.VolunteerNicknameAndId;
import com.somemore.volunteer.usecase.NEWVolunteerQueryUseCase;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReviewQueryService implements ReviewQueryUseCase {

    private final ReviewRepository reviewRepository;
    private final NEWVolunteerQueryUseCase volunteerQueryUseCase;
    private final VolunteerApplyQueryUseCase volunteerApplyQueryUseCase;

    @Override
    public boolean existsByVolunteerApplyId(Long volunteerApplyId) {
        return reviewRepository.existsByVolunteerApplyId(volunteerApplyId);
    }

    @Override
    public Review getById(Long id) {
        return reviewRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException(NOT_EXISTS_REVIEW));
    }

    @Override
    public ReviewDetailResponseDto getDetailById(Long id) {
        Review review = getById(id);
        Long recruitBoardId = volunteerApplyQueryUseCase.getRecruitBoardIdById(
                review.getVolunteerApplyId());
        return ReviewDetailResponseDto.of(review, recruitBoardId);
    }

    @Override
    public Page<ReviewDetailWithNicknameResponseDto> getDetailsWithNicknameByVolunteerId(
            UUID volunteerId,
            ReviewSearchCondition condition
    ) {
        String nickname = volunteerQueryUseCase.getNicknameById(volunteerId);
        Page<Review> reviews = reviewRepository.findAllByVolunteerIdAndSearch(volunteerId,
                condition);

        return reviews.map(
                review -> ReviewDetailWithNicknameResponseDto.of(review, nickname)
        );
    }

    @Override
    public Page<ReviewDetailWithNicknameResponseDto> getDetailsWithNicknameByCenterId(
            UUID centerId,
            ReviewSearchCondition condition
    ) {
        Page<Review> reviews = reviewRepository.findAllByCenterIdAndSearch(centerId, condition);
        List<UUID> volunteerIds = reviews.get().map(Review::getVolunteerId).toList();
        Map<UUID, String> volunteerNicknames = mapVolunteerIdsToNicknames(volunteerIds);

        return reviews.map(review ->
                ReviewDetailWithNicknameResponseDto.of(review,
                        volunteerNicknames.get(review.getVolunteerId()))
        );
    }

    private Map<UUID, String> mapVolunteerIdsToNicknames(List<UUID> volunteerIds) {
        return volunteerQueryUseCase.getVolunteerNicknameAndIdsByIds(volunteerIds)
                .stream()
                .collect(Collectors.toMap(VolunteerNicknameAndId::id,
                        VolunteerNicknameAndId::nickname));
    }
}
