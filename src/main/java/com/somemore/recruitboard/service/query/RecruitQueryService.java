package com.somemore.recruitboard.service.query;

import com.somemore.global.exception.BadRequestException;
import com.somemore.recruitboard.domain.RecruitBoard;
import com.somemore.recruitboard.repository.RecruitBoardRepository;
import com.somemore.recruitboard.usecase.query.RecruitQueryUseCase;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RecruitQueryService implements RecruitQueryUseCase {

    private final RecruitBoardRepository recruitBoardRepository;

    @Override
    public Optional<RecruitBoard> findById(Long id) {
        return recruitBoardRepository.findById(id);
    }

    @Override
    public RecruitBoard findByIdOrThrow(Long id) {
        return recruitBoardRepository.findById(id).orElseThrow(
            () -> new BadRequestException("존재하지 않는 봉사 모집 활동입니다.")
        );
    }

}
