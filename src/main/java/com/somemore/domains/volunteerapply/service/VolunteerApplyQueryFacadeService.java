package com.somemore.domains.volunteerapply.service;

import com.somemore.domains.recruitboard.domain.RecruitBoard;
import com.somemore.domains.recruitboard.service.validator.RecruitBoardValidator;
import com.somemore.domains.recruitboard.usecase.RecruitBoardQueryUseCase;
import com.somemore.domains.review.usecase.ReviewQueryUseCase;
import com.somemore.domains.volunteerapply.domain.VolunteerApply;
import com.somemore.domains.volunteerapply.dto.condition.VolunteerApplySearchCondition;
import com.somemore.domains.volunteerapply.dto.response.VolunteerApplyRecruitInfoResponseDto;
import com.somemore.domains.volunteerapply.dto.response.VolunteerApplyVolunteerInfoResponseDto;
import com.somemore.domains.volunteerapply.dto.response.VolunteerApplyWithReviewStatusResponseDto;
import com.somemore.domains.volunteerapply.usecase.VolunteerApplyQueryFacadeUseCase;
import com.somemore.domains.volunteerapply.usecase.VolunteerApplyQueryUseCase;
import com.somemore.user.domain.UserCommonAttribute;
import com.somemore.user.usecase.UserQueryUseCase;
import com.somemore.volunteer.dto.VolunteerInfoResponseDto;
import com.somemore.volunteer.repository.record.VolunteerNicknameAndId;
import com.somemore.volunteer.usecase.NEWVolunteerQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class VolunteerApplyQueryFacadeService implements VolunteerApplyQueryFacadeUseCase {

    private final VolunteerApplyQueryUseCase volunteerApplyQueryUseCase;
    private final RecruitBoardQueryUseCase recruitBoardQueryUseCase;
    private final NEWVolunteerQueryUseCase volunteerQueryUseCase;
    private final UserQueryUseCase userQueryUseCase;
    private final ReviewQueryUseCase reviewQueryUseCase;
    private final RecruitBoardValidator recruitBoardValidator;

    @Override
    public VolunteerApplyWithReviewStatusResponseDto getVolunteerApplyByRecruitIdAndVolunteerId(
            Long recruitBoardId,
            UUID volunteerId
    ) {
        VolunteerApply apply = volunteerApplyQueryUseCase.getByRecruitIdAndVolunteerId(recruitBoardId, volunteerId);
        boolean isReviewed = checkIfReviewed(apply);

        return VolunteerApplyWithReviewStatusResponseDto.of(apply, isReviewed);
    }

    @Override
    public Page<VolunteerApplyVolunteerInfoResponseDto> getVolunteerAppliesByRecruitIdAndCenterId(
            Long recruitBoardId,
            UUID centerId,
            VolunteerApplySearchCondition condition
    ) {
        RecruitBoard recruitBoard = recruitBoardQueryUseCase.getById(recruitBoardId);
        recruitBoardValidator.validateWriter(recruitBoard, centerId);

        Page<VolunteerApply> applies = volunteerApplyQueryUseCase.getAllByRecruitId(recruitBoardId, condition);
        Map<UUID, VolunteerInfoResponseDto> volunteerMapToVolunteerId = getVolunteerInfoMap(applies);

        return applies.map(apply -> VolunteerApplyVolunteerInfoResponseDto.of(
                apply,
                volunteerMapToVolunteerId.get(apply.getVolunteerId())
        ));
    }

    @Override
    public Page<VolunteerApplyRecruitInfoResponseDto> getVolunteerAppliesByVolunteerId(
            UUID volunteerId,
            VolunteerApplySearchCondition condition
    ) {
        Page<VolunteerApply> applies = volunteerApplyQueryUseCase.getAllByVolunteerId(volunteerId, condition);
        Map<Long, RecruitBoard> boardMapToId = getRecruitBoardMap(applies);

        return applies.map(apply -> VolunteerApplyRecruitInfoResponseDto.of(
                apply,
                boardMapToId.get(apply.getRecruitBoardId()))
        );
    }

    private boolean checkIfReviewed(VolunteerApply apply) {
        return apply.isVolunteerActivityCompleted()
                && reviewQueryUseCase.existsByVolunteerApplyId(apply.getId());
    }

    private Map<Long, RecruitBoard> getRecruitBoardMap(Page<VolunteerApply> applies) {
        // 1. 모집글 ID 추출
        List<Long> boardIds = applies.getContent().stream()
                .map(VolunteerApply::getRecruitBoardId)
                .toList();

        // 2. 모집글 ID가 비어 있는 경우 빈 맵 반환
        if (boardIds.isEmpty()) {
            return Map.of();
        }

        // 3. ID로 모집글 조회
        List<RecruitBoard> boards = recruitBoardQueryUseCase.getAllByIds(boardIds);

        // 4. 조회된 모집글을 Map 변환
        return boards.stream()
                .collect(Collectors.toMap(RecruitBoard::getId, Function.identity()));
    }

    private Map<UUID, VolunteerInfoResponseDto> getVolunteerInfoMap(Page<VolunteerApply> applies) {
        // 1. Volunteer IDs 추출
        List<UUID> volunteerIds = applies.getContent().stream()
                .map(VolunteerApply::getVolunteerId)
                .toList();

        // 2. Volunteer 정보 조회
        List<VolunteerNicknameAndId> volunteerNicknameAndIds =
                volunteerQueryUseCase.getVolunteerNicknameAndIdsByIds(volunteerIds);

        // 3. User 정보 조회
        Map<UUID, UserCommonAttribute> userAttributesMap =
                userQueryUseCase.getAllByUserIds(
                                volunteerNicknameAndIds.stream()
                                        .map(VolunteerNicknameAndId::userId)
                                        .toList()
                        ).stream()
                        .collect(Collectors.toMap(UserCommonAttribute::getUserId, user -> user));

        // 4. VolunteerInfoResponseDto 생성 및 Map 변환
        return volunteerNicknameAndIds.stream()
                .map(volunteer -> VolunteerInfoResponseDto.of(
                        volunteer,
                        userAttributesMap.get(volunteer.userId())
                ))
                .collect(Collectors.toMap(VolunteerInfoResponseDto::id, Function.identity()));
    }
}
