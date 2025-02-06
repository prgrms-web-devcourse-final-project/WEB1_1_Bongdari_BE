package com.somemore.domains.volunteerapply.service;

import com.somemore.domains.recruitboard.domain.RecruitBoard;
import com.somemore.domains.recruitboard.usecase.RecruitBoardQueryUseCase;
import com.somemore.domains.volunteerapply.domain.VolunteerApply;
import com.somemore.domains.volunteerapply.dto.request.VolunteerApplyCreateRequestDto;
import com.somemore.domains.volunteerapply.event.VolunteerApplyEvent;
import com.somemore.domains.volunteerapply.repository.VolunteerApplyRepository;
import com.somemore.domains.volunteerapply.usecase.ApplyVolunteerApplyUseCase;
import com.somemore.global.common.event.ServerEventPublisher;
import com.somemore.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.somemore.global.exception.ExceptionMessage.DUPLICATE_APPLICATION;
import static com.somemore.global.exception.ExceptionMessage.RECRUITMENT_NOT_OPEN;

@RequiredArgsConstructor
@Transactional
@Service
public class ApplyVolunteerApplyService implements ApplyVolunteerApplyUseCase {

    private final VolunteerApplyRepository volunteerApplyRepository;
    private final RecruitBoardQueryUseCase recruitBoardQueryUseCase;
    private final ServerEventPublisher serverEventPublisher;

    @Override
    public Long apply(VolunteerApplyCreateRequestDto requestDto, UUID volunteerId) {

        RecruitBoard board = recruitBoardQueryUseCase.getById(requestDto.recruitBoardId());
        validateCanApply(board);
        validateDuplicatedApply(volunteerId, board);

        VolunteerApply apply = requestDto.toEntity(volunteerId);
        volunteerApplyRepository.save(apply);

        publishVolunteerApplyEvent(apply, board);

        return apply.getId();
    }

    private void validateCanApply(RecruitBoard board) {
        if (board.isRecruiting()) {
            return;
        }
        throw new BadRequestException(RECRUITMENT_NOT_OPEN);
    }

    private void validateDuplicatedApply(UUID volunteerId, RecruitBoard board) {
        boolean isDuplicate = volunteerApplyRepository.existsByRecruitIdAndVolunteerId(board.getId(),
                volunteerId);
        if (isDuplicate) {
            throw new BadRequestException(DUPLICATE_APPLICATION);
        }
    }

    private void publishVolunteerApplyEvent(VolunteerApply apply, RecruitBoard board) {
        VolunteerApplyEvent event = VolunteerApplyEvent.of(apply, board);
        serverEventPublisher.publish(event);
    }
}
