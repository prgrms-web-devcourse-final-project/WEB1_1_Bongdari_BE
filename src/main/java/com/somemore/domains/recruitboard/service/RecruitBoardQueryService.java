package com.somemore.domains.recruitboard.service;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_RECRUIT_BOARD;

import com.somemore.domains.recruitboard.domain.RecruitBoard;
import com.somemore.domains.recruitboard.dto.condition.RecruitBoardNearByCondition;
import com.somemore.domains.recruitboard.dto.condition.RecruitBoardSearchCondition;
import com.somemore.domains.recruitboard.dto.response.RecruitBoardDetailResponseDto;
import com.somemore.domains.recruitboard.dto.response.RecruitBoardResponseDto;
import com.somemore.domains.recruitboard.dto.response.RecruitBoardWithCenterResponseDto;
import com.somemore.domains.recruitboard.dto.response.RecruitBoardWithLocationResponseDto;
import com.somemore.domains.recruitboard.repository.RecruitBoardRepository;
import com.somemore.domains.recruitboard.repository.mapper.RecruitBoardDetail;
import com.somemore.domains.recruitboard.repository.mapper.RecruitBoardWithCenter;
import com.somemore.domains.recruitboard.repository.mapper.RecruitBoardWithLocation;
import com.somemore.domains.recruitboard.usecase.RecruitBoardQueryUseCase;
import com.somemore.global.exception.BadRequestException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RecruitBoardQueryService implements RecruitBoardQueryUseCase {

    private final RecruitBoardRepository recruitBoardRepository;

    @Override
    public RecruitBoard getById(Long id) {
        return recruitBoardRepository.findById(id).orElseThrow(
                () -> new BadRequestException(NOT_EXISTS_RECRUIT_BOARD.getMessage())
        );
    }

    @Override
    public RecruitBoardResponseDto getRecruitBoardById(Long id) {
        RecruitBoard recruitBoard = getById(id);
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
            RecruitBoardSearchCondition condition) {
        Page<RecruitBoard> boards = recruitBoardRepository.findAllByCenterId(centerId, condition);
        return boards.map(RecruitBoardResponseDto::from);
    }

    @Override
    public List<Long> getNotCompletedIdsByCenterIds(UUID centerId) {
        return recruitBoardRepository.findNotCompletedIdsByCenterId(centerId);
    }

    @Override
    public List<RecruitBoard> getAllByIds(List<Long> ids) {
        return recruitBoardRepository.findAllByIds(ids);
    }

    @Override
    public List<RecruitBoard> getAllRecruitBoards() {
        return recruitBoardRepository.findAll();
    }
}
