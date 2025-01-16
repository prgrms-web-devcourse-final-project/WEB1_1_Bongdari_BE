package com.somemore.domains.recruitboard.repository.mapper;

import com.somemore.domains.recruitboard.domain.RecruitBoard;

import java.math.BigDecimal;

public record RecruitBoardWithLocation(
    RecruitBoard recruitBoard,
    String address,
    BigDecimal latitude,
    BigDecimal longitude
) {

}
