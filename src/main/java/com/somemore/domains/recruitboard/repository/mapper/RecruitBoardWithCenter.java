package com.somemore.domains.recruitboard.repository.mapper;

import com.somemore.domains.recruitboard.domain.RecruitBoard;

public record RecruitBoardWithCenter(
        RecruitBoard recruitBoard,
        String centerName
) {

}
