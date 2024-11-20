package com.somemore.recruitboard.service;

import com.somemore.recruitboard.domain.RecruitBoard;
import com.somemore.recruitboard.repository.RecruitBoardRepository;
import com.somemore.recruitboard.dto.command.RecruitBoardCreateCommandRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class RecruitBoardCommander implements RecruitBoardCommandService {

    private final RecruitBoardRepository recruitBoardRepository;

    @Override
    public Long create(RecruitBoardCreateCommandRequestDto dto) {
        RecruitBoard recruitBoard = dto.toEntity();
        recruitBoardRepository.save(recruitBoard);

        return recruitBoard.getId();
    }

}
