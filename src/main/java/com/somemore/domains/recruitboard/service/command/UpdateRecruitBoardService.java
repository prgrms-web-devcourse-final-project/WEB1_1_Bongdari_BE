package com.somemore.domains.recruitboard.service.command;

import com.somemore.domains.location.usecase.command.UpdateLocationUseCase;
import com.somemore.domains.recruitboard.domain.RecruitBoard;
import com.somemore.domains.recruitboard.domain.RecruitStatus;
import com.somemore.domains.recruitboard.dto.request.RecruitBoardLocationUpdateRequestDto;
import com.somemore.domains.recruitboard.dto.request.RecruitBoardUpdateRequestDto;
import com.somemore.domains.recruitboard.repository.RecruitBoardRepository;
import com.somemore.domains.recruitboard.service.validator.RecruitBoardValidator;
import com.somemore.domains.recruitboard.usecase.command.UpdateRecruitBoardUseCase;
import com.somemore.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_RECRUIT_BOARD;

@RequiredArgsConstructor
@Transactional
@Service
public class UpdateRecruitBoardService implements UpdateRecruitBoardUseCase {

    private final RecruitBoardRepository recruitBoardRepository;
    private final RecruitBoardValidator recruitBoardValidator;
    private final UpdateLocationUseCase updateLocationUseCase;

    @Override
    public void updateRecruitBoard(RecruitBoardUpdateRequestDto dto, Long recruitBoardId, UUID centerId, String imgUrl) {
        RecruitBoard recruitBoard = getRecruitBoard(recruitBoardId);
        recruitBoardValidator.validateAuthor(recruitBoard, centerId);
        recruitBoardValidator.validateRecruitBoardTime(dto.volunteerStartDateTime(), dto.volunteerEndDateTime());

        recruitBoard.updateWith(dto, imgUrl);
    }

    @Override
    public void updateRecruitBoardLocation(RecruitBoardLocationUpdateRequestDto requestDto, Long recruitBoardId,
                                           UUID centerId) {
        RecruitBoard recruitBoard = getRecruitBoard(recruitBoardId);
        recruitBoardValidator.validateAuthor(recruitBoard, centerId);

        updateLocationUseCase.updateLocation(requestDto.toLocationUpdateRequestDto(), recruitBoard.getLocationId());

        recruitBoard.updateWith(requestDto.region());
    }

    @Override
    public void updateRecruitBoardStatus(RecruitStatus status, Long recruitBoardId, UUID centerId,
                                         LocalDateTime currentDateTime) {
        RecruitBoard recruitBoard = getRecruitBoard(recruitBoardId);
        recruitBoardValidator.validateAuthor(recruitBoard, centerId);

        recruitBoard.changeRecruitStatus(status, currentDateTime);
    }

    private RecruitBoard getRecruitBoard(Long recruitBoardId) {
        return recruitBoardRepository.findById(recruitBoardId).orElseThrow(
                () -> new BadRequestException(NOT_EXISTS_RECRUIT_BOARD.getMessage())
        );
    }

}
