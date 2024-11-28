package com.somemore.recruitboard.domain.mapping;

import com.somemore.recruitboard.domain.RecruitBoard;
import java.math.BigDecimal;

public record RecruitBoardDetail(
    RecruitBoard recruitBoard,
    String address,
    BigDecimal latitude,
    BigDecimal longitude,
    String centerName
) {

}
