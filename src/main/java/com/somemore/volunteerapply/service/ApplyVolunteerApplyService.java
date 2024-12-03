package com.somemore.volunteerapply.service;

import static com.somemore.global.exception.ExceptionMessage.DUPLICATE_APPLICATION;
import static com.somemore.global.exception.ExceptionMessage.RECRUITMENT_NOT_OPEN;

import com.somemore.global.exception.BadRequestException;
import com.somemore.recruitboard.domain.RecruitBoard;
import com.somemore.recruitboard.usecase.query.RecruitBoardQueryUseCase;
import com.somemore.volunteerapply.domain.VolunteerApply;
import com.somemore.volunteerapply.dto.VolunteerApplyCreateRequestDto;
import com.somemore.volunteerapply.repository.VolunteerApplyRepository;
import com.somemore.volunteerapply.usecase.ApplyVolunteerApplyUseCase;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class ApplyVolunteerApplyService implements ApplyVolunteerApplyUseCase {

    private final VolunteerApplyRepository volunteerApplyRepository;
    private final RecruitBoardQueryUseCase recruitBoardQueryUseCase;

    @Override
    public Long apply(VolunteerApplyCreateRequestDto requestDto, UUID volunteerId) {

        RecruitBoard board = recruitBoardQueryUseCase.getById(requestDto.recruitBoardId());
        validateCanApply(board);
        validateDuplicatedApply(volunteerId, board);

        VolunteerApply apply = requestDto.toEntity(volunteerId);
        volunteerApplyRepository.save(apply);

        return apply.getId();
    }

    private void validateCanApply(RecruitBoard board) {
        if (board.isRecruitOpen()) {
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
}
