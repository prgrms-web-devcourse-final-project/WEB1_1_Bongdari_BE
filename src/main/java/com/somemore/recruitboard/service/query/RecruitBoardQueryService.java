package com.somemore.recruitboard.service.query;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_RECRUIT_BOARD;

import com.somemore.global.exception.BadRequestException;
import com.somemore.global.exception.ExceptionMessage;
import com.somemore.recruitboard.domain.RecruitBoard;
import com.somemore.recruitboard.repository.RecruitBoardRepository;
import com.somemore.recruitboard.usecase.query.RecruitBoardQueryUseCase;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
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

}
