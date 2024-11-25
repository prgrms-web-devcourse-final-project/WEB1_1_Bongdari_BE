package com.somemore.recruitboard.service.command;

import com.somemore.global.exception.BadRequestException;
import com.somemore.location.usecase.command.UpdateLocationUseCase;
import com.somemore.recruitboard.domain.RecruitBoard;
import com.somemore.recruitboard.dto.request.RecruitBoardLocationUpdateRequestDto;
import com.somemore.recruitboard.dto.request.RecruitBoardUpdateRequestDto;
import com.somemore.recruitboard.repository.RecruitBoardRepository;
import com.somemore.recruitboard.usecase.command.UpdateRecruitBoardUseCase;
import com.somemore.recruitboard.usecase.query.RecruitQueryUseCase;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Transactional
@Service
public class UpdateRecruitBoardService implements UpdateRecruitBoardUseCase {

    private final RecruitBoardRepository recruitBoardRepository;
    private final RecruitQueryUseCase recruitQueryUseCase;
    private final UpdateLocationUseCase updateLocationUseCase;

    @Override
    public void updateRecruitBoard(
        RecruitBoardUpdateRequestDto requestDto,
        Long recruitBoardId,
        UUID centerId,
        String imgUrl) {

        RecruitBoard recruitBoard = recruitQueryUseCase.findByIdOrThrow(recruitBoardId);
        validateWriter(recruitBoard, centerId);
        recruitBoard.updateWith(requestDto, imgUrl);

        recruitBoardRepository.save(recruitBoard);
    }

    @Override
    public void updateRecruitBoardLocation(RecruitBoardLocationUpdateRequestDto requestDto,
        Long recruitBoardId, UUID centerId) {

        RecruitBoard recruitBoard = recruitQueryUseCase.findByIdOrThrow(recruitBoardId);
        validateWriter(recruitBoard, centerId);

        updateLocationUseCase.updateLocation(requestDto.toLocationUpdateRequestDto(),
            recruitBoard.getLocationId());

        recruitBoard.updateWith(requestDto.region());
        recruitBoardRepository.save(recruitBoard);
    }

    private void validateWriter(RecruitBoard recruitBoard, UUID centerId) {
        if (recruitBoard.isNotWriter(centerId)) {
            throw new BadRequestException("자신이 작성한 봉사 모집글만 수정할 수 있습니다.");
        }

    }
}
