package com.somemore.facade.volunteerapply;

import static com.somemore.global.exception.ExceptionMessage.UNAUTHORIZED_RECRUIT_BOARD;

import com.somemore.global.exception.BadRequestException;
import com.somemore.recruitboard.domain.RecruitBoard;
import com.somemore.recruitboard.usecase.query.RecruitBoardQueryUseCase;
import com.somemore.volunteer.repository.mapper.VolunteerSimpleInfo;
import com.somemore.volunteer.usecase.VolunteerQueryUseCase;
import com.somemore.volunteerapply.domain.VolunteerApply;
import com.somemore.volunteerapply.dto.condition.VolunteerApplySearchCondition;
import com.somemore.volunteerapply.dto.response.VolunteerApplyRecruitInfoResponseDto;
import com.somemore.volunteerapply.dto.response.VolunteerApplyVolunteerInfoResponseDto;
import com.somemore.volunteerapply.repository.VolunteerApplyRepository;
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
public class VolunteerApplyQueryFacadeService implements VolunteerApplyQueryFacadeUseCase {

    private final VolunteerApplyRepository volunteerApplyRepository;
    private final RecruitBoardQueryUseCase recruitBoardQueryUseCase;
    private final VolunteerQueryUseCase volunteerQueryUseCase;

    @Override
    public Page<VolunteerApplyVolunteerInfoResponseDto> getVolunteerAppliesByRecruitIdAndCenterId(
            Long recruitId, UUID centerId, VolunteerApplySearchCondition condition) {
        validateAuthorization(recruitId, centerId);

        Page<VolunteerApply> applies = volunteerApplyRepository.findAllByRecruitId(recruitId,
                condition);

        Map<UUID, VolunteerSimpleInfo> volunteerMap = getVolunteerInfoMap(
                applies);

        return applies.map(
                apply -> VolunteerApplyVolunteerInfoResponseDto.of(
                        apply,
                        volunteerMap.getOrDefault(apply.getVolunteerId(), null)
                ));
    }

    @Override
    public Page<VolunteerApplyRecruitInfoResponseDto> getVolunteerAppliesByVolunteerId(
            UUID volunteerId, VolunteerApplySearchCondition condition) {

        Page<VolunteerApply> applies = volunteerApplyRepository.findAllByVolunteerId(volunteerId,
                condition);

        Map<Long, RecruitBoard> boardMap = getRecruitBoardMap(applies);

        return applies.map(
                apply -> VolunteerApplyRecruitInfoResponseDto.of(
                        apply,
                        boardMap.getOrDefault(apply.getRecruitBoardId(), null)
                ));
    }

    private void validateAuthorization(Long recruitId, UUID centerId) {
        RecruitBoard recruitBoard = recruitBoardQueryUseCase.getById(recruitId);
        if (recruitBoard.isWriter(centerId)) {
            return;
        }

        throw new BadRequestException(UNAUTHORIZED_RECRUIT_BOARD);
    }

    private Map<Long, RecruitBoard> getRecruitBoardMap(Page<VolunteerApply> applies) {
        List<Long> boardIds = applies.getContent().stream().map(VolunteerApply::getRecruitBoardId)
                .toList();
        List<RecruitBoard> boards = recruitBoardQueryUseCase.getAllByIds(boardIds);

        return boards.stream()
                .collect(Collectors.toMap(RecruitBoard::getId,
                        board -> board));
    }

    private Map<UUID, VolunteerSimpleInfo> getVolunteerInfoMap(Page<VolunteerApply> applies) {
        List<UUID> volunteerIds = applies.getContent().stream().map(VolunteerApply::getVolunteerId)
                .toList();

        List<VolunteerSimpleInfo> volunteers = volunteerQueryUseCase.getVolunteerSimpleInfosByIds(
                volunteerIds);

        return volunteers.stream()
                .collect(Collectors.toMap(VolunteerSimpleInfo::id,
                        volunteer -> volunteer));
    }

}
