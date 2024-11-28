package com.somemore.recruitboard.usecase.query;

import com.somemore.recruitboard.domain.RecruitBoard;
import java.util.Optional;

public interface RecruitBoardQueryUseCase {

    RecruitBoard getById(Long id);

}
