package com.somemore.domains.recruitboard.repository.mapper;

import com.somemore.domains.recruitboard.domain.RecruitBoard;
import java.math.BigDecimal;

public record RecruitBoardDetail(
    RecruitBoard recruitBoard,
    String address,
    BigDecimal latitude,
    BigDecimal longitude,
    String centerName
) {

}
