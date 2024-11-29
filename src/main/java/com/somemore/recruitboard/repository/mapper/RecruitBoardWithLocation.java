package com.somemore.recruitboard.repository.mapper;

import com.somemore.recruitboard.domain.RecruitBoard;
import java.math.BigDecimal;

public record RecruitBoardWithLocation(
    RecruitBoard recruitBoard,
    String address,
    BigDecimal latitude,
    BigDecimal longitude
) {

}
