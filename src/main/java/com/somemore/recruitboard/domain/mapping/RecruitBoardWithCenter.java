package com.somemore.recruitboard.domain.mapping;

import com.somemore.recruitboard.domain.RecruitBoard;

public record RecruitBoardWithCenter(
    RecruitBoard recruitBoard,
    String centerName
) {

}
