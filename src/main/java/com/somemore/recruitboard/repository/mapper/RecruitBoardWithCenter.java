package com.somemore.recruitboard.repository.mapper;

import com.somemore.recruitboard.domain.RecruitBoard;

public record RecruitBoardWithCenter(
    RecruitBoard recruitBoard,
    String centerName
) {

}
