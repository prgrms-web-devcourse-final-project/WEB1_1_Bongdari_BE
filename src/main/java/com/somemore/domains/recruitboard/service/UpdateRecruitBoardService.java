package com.somemore.domains.recruitboard.service;

import com.somemore.domains.location.usecase.command.UpdateLocationUseCase;
import com.somemore.domains.recruitboard.domain.RecruitBoard;
import com.somemore.domains.recruitboard.domain.RecruitStatus;
import com.somemore.domains.recruitboard.dto.request.RecruitBoardLocationUpdateRequestDto;
import com.somemore.domains.recruitboard.dto.request.RecruitBoardUpdateRequestDto;
import com.somemore.domains.recruitboard.service.validator.RecruitBoardValidator;
import com.somemore.domains.recruitboard.usecase.RecruitBoardQueryUseCase;
import com.somemore.domains.recruitboard.usecase.UpdateRecruitBoardUseCase;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class UpdateRecruitBoardService implements UpdateRecruitBoardUseCase {

    private final RecruitBoardQueryUseCase recruitBoardQueryUseCase;
    private final UpdateLocationUseCase updateLocationUseCase;
    private final RecruitBoardValidator recruitBoardValidator;
    private final Clock clock;

    @Override
    public void updateRecruitBoard(RecruitBoardUpdateRequestDto dto, Long id, UUID centerId,
            String imgUrl) {
        RecruitBoard recruitBoard = getRecruitBoard(id);
        validateUpdatableAndWriter(recruitBoard, centerId);

        recruitBoardValidator.validateUpdateRecruitBoardTime(recruitBoard.getCreatedAt(),
                dto.volunteerStartDateTime(), dto.volunteerEndDateTime());

        recruitBoard.updateWith(dto, imgUrl);
    }

    @Override
    public void updateRecruitBoardLocation(RecruitBoardLocationUpdateRequestDto requestDto, Long id,
            UUID centerId) {
        RecruitBoard recruitBoard = getRecruitBoard(id);
        validateUpdatableAndWriter(recruitBoard, centerId);

        updateLocationUseCase.updateLocation(requestDto.toLocationUpdateRequestDto(),
                recruitBoard.getLocationId());
        recruitBoard.updateWith(requestDto.region());
    }

    @Override
    public void updateRecruitBoardStatus(RecruitStatus status, Long id, UUID centerId) {
        RecruitBoard recruitBoard = getRecruitBoard(id);
        validateUpdatableAndWriter(recruitBoard, centerId);
        recruitBoardValidator.validateRecruitStatus(status);

        recruitBoard.updateRecruitStatus(status);
    }

    private void validateUpdatableAndWriter(RecruitBoard recruitBoard, UUID centerId) {
        LocalDateTime current = LocalDateTime.now(clock);
        recruitBoardValidator.validateUpdatable(recruitBoard, current);
        recruitBoardValidator.validateWriter(recruitBoard, centerId);
    }

    private RecruitBoard getRecruitBoard(Long id) {
        return recruitBoardQueryUseCase.getById(id);
    }
}

