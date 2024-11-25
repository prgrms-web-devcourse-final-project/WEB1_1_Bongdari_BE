package com.somemore.recruitboard.usecase.query;

import com.somemore.recruitboard.domain.RecruitBoard;
import java.util.Optional;

public interface RecruitQueryUseCase {

    Optional<RecruitBoard> findById(Long id);

    RecruitBoard findByIdOrThrow(Long id);

}
