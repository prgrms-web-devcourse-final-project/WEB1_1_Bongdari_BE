package com.somemore.recruitboard.service.query;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_RECRUIT_BOARD;

import com.somemore.global.exception.BadRequestException;
import com.somemore.recruitboard.domain.RecruitBoard;
import com.somemore.recruitboard.domain.mapping.RecruitBoardDetail;
import com.somemore.recruitboard.domain.mapping.RecruitBoardWithCenter;
import com.somemore.recruitboard.domain.mapping.RecruitBoardWithLocation;
import com.somemore.recruitboard.dto.condition.RecruitBoardNearByCondition;
import com.somemore.recruitboard.dto.condition.RecruitBoardSearchCondition;
import com.somemore.recruitboard.dto.response.RecruitBoardDetailResponseDto;
import com.somemore.recruitboard.dto.response.RecruitBoardResponseDto;
import com.somemore.recruitboard.dto.response.RecruitBoardWithCenterResponseDto;
import com.somemore.recruitboard.dto.response.RecruitBoardWithLocationResponseDto;
import com.somemore.recruitboard.repository.RecruitBoardRepository;
import com.somemore.recruitboard.usecase.query.RecruitBoardQueryUseCase;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RecruitBoardQueryService implements RecruitBoardQueryUseCase {

    private final RecruitBoardRepository recruitBoardRepository;

    @Override
    public RecruitBoardResponseDto getById(Long id) {
        RecruitBoard recruitBoard = recruitBoardRepository.findById(id).orElseThrow(
            () -> new BadRequestException(NOT_EXISTS_RECRUIT_BOARD.getMessage())
        );
        return RecruitBoardResponseDto.from(recruitBoard);
    }

    @Override
    public RecruitBoardWithLocationResponseDto getWithLocationById(Long id) {
        RecruitBoardWithLocation recruitBoardWithLocation = recruitBoardRepository.findWithLocationById(
            id).orElseThrow(
            () -> new BadRequestException(NOT_EXISTS_RECRUIT_BOARD.getMessage())
        );
        return RecruitBoardWithLocationResponseDto.from(recruitBoardWithLocation);
    }

    @Override
    public Page<RecruitBoardWithCenterResponseDto> getAllWithCenter(
        RecruitBoardSearchCondition condition) {
        Page<RecruitBoardWithCenter> boards = recruitBoardRepository.findAllWithCenter(condition);
        return boards.map(RecruitBoardWithCenterResponseDto::from);
    }

    @Override
    public Page<RecruitBoardDetailResponseDto> getRecruitBoardsNearby(
        RecruitBoardNearByCondition condition) {
        Page<RecruitBoardDetail> boards = recruitBoardRepository.findAllNearby(condition);
        return boards.map(RecruitBoardDetailResponseDto::from);
    }

    @Override
    public Page<RecruitBoardResponseDto> getRecruitBoardsByCenterId(UUID centerId,
        Pageable pageable) {
        Page<RecruitBoard> boards = recruitBoardRepository.findAllByCenterId(centerId, pageable);
        return boards.map(RecruitBoardResponseDto::from);
    }

}
