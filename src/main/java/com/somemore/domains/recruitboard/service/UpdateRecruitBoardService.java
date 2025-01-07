package com.somemore.domains.recruitboard.service;

import com.somemore.domains.location.usecase.command.UpdateLocationUseCase;
import com.somemore.domains.recruitboard.domain.RecruitBoard;
import com.somemore.domains.recruitboard.domain.RecruitStatus;
import com.somemore.domains.recruitboard.dto.request.RecruitBoardLocationUpdateRequestDto;
import com.somemore.domains.recruitboard.dto.request.RecruitBoardUpdateRequestDto;
import com.somemore.domains.recruitboard.service.validator.RecruitBoardValidator;
import com.somemore.domains.recruitboard.usecase.RecruitBoardQueryUseCase;
import com.somemore.domains.recruitboard.usecase.UpdateRecruitBoardUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Transactional
@Service
public class UpdateRecruitBoardService implements UpdateRecruitBoardUseCase {

    private final RecruitBoardQueryUseCase recruitBoardQueryUseCase;
    private final UpdateLocationUseCase updateLocationUseCase;
    private final RecruitBoardValidator recruitBoardValidator;

    @Override
    public void updateRecruitBoard(RecruitBoardUpdateRequestDto dto, Long id, UUID centerId, String imgUrl) {
        RecruitBoard recruitBoard = recruitBoardQueryUseCase.getById(id);
        recruitBoardValidator.validateWriter(recruitBoard, centerId);
        recruitBoardValidator.validateRecruitBoardTime(dto.volunteerStartDateTime(), dto.volunteerEndDateTime());

        recruitBoard.updateWith(dto, imgUrl);
    }

    @Override
    public void updateRecruitBoardLocation(RecruitBoardLocationUpdateRequestDto requestDto, Long id, UUID centerId) {
        RecruitBoard recruitBoard = recruitBoardQueryUseCase.getById(id);
        recruitBoardValidator.validateWriter(recruitBoard, centerId);

        updateLocationUseCase.updateLocation(requestDto.toLocationUpdateRequestDto(), recruitBoard.getLocationId());

        recruitBoard.updateWith(requestDto.region());
    }

    @Override
    public void updateRecruitBoardStatus(RecruitStatus status, Long id, UUID centerId, LocalDateTime currentDateTime) {
        RecruitBoard recruitBoard = recruitBoardQueryUseCase.getById(id);
        recruitBoardValidator.validateWriter(recruitBoard, centerId);

        recruitBoard.changeRecruitStatus(status, currentDateTime);
    }

}
