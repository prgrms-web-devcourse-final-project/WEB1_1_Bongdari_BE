package com.somemore.recruitboard.service.command;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_RECRUIT_BOARD;
import static com.somemore.global.exception.ExceptionMessage.UNAUTHORIZED_RECRUIT_BOARD;

import com.somemore.global.exception.BadRequestException;
import com.somemore.location.usecase.command.UpdateLocationUseCase;
import com.somemore.recruitboard.domain.RecruitBoard;
import com.somemore.recruitboard.dto.request.RecruitBoardLocationUpdateRequestDto;
import com.somemore.recruitboard.dto.request.RecruitBoardUpdateRequestDto;
import com.somemore.recruitboard.repository.RecruitBoardRepository;
import com.somemore.recruitboard.usecase.command.UpdateRecruitBoardUseCase;
import com.somemore.recruitboard.usecase.query.RecruitBoardQueryUseCase;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Transactional
@Service
public class UpdateRecruitBoardService implements UpdateRecruitBoardUseCase {

    private final RecruitBoardRepository recruitBoardRepository;
    private final RecruitBoardQueryUseCase recruitBoardQueryUseCase;
    private final UpdateLocationUseCase updateLocationUseCase;

    @Override
    public void updateRecruitBoard(
        RecruitBoardUpdateRequestDto requestDto,
        Long recruitBoardId,
        UUID centerId,
        String imgUrl) {

        RecruitBoard recruitBoard = recruitBoardQueryUseCase.findById(recruitBoardId).orElseThrow(
            () -> new BadRequestException(NOT_EXISTS_RECRUIT_BOARD.getMessage())
        );
        validateWriter(recruitBoard, centerId);
        recruitBoard.updateWith(requestDto, imgUrl);

        recruitBoardRepository.save(recruitBoard);
    }

    @Override
    public void updateRecruitBoardLocation(RecruitBoardLocationUpdateRequestDto requestDto,
        Long recruitBoardId, UUID centerId) {

        RecruitBoard recruitBoard = recruitBoardQueryUseCase.findById(recruitBoardId).orElseThrow(
            () -> new BadRequestException(NOT_EXISTS_RECRUIT_BOARD.getMessage())
        );
        validateWriter(recruitBoard, centerId);

        updateLocationUseCase.updateLocation(requestDto.toLocationUpdateRequestDto(),
            recruitBoard.getLocationId());

        recruitBoard.updateWith(requestDto.region());
        recruitBoardRepository.save(recruitBoard);
    }

    private void validateWriter(RecruitBoard recruitBoard, UUID centerId) {
        if (recruitBoard.isNotWriter(centerId)) {
            throw new BadRequestException(UNAUTHORIZED_RECRUIT_BOARD.getMessage());
        }
    }
}
