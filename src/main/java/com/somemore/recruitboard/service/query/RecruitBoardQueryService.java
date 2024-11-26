package com.somemore.recruitboard.service.query;

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
    public Optional<RecruitBoard> findById(Long id) {
        return recruitBoardRepository.findById(id);
    }

}
