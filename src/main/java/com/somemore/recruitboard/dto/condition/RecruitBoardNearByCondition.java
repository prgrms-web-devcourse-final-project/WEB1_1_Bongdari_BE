package com.somemore.recruitboard.dto.condition;

import lombok.Builder;
import org.springframework.data.domain.Pageable;

@Builder
public record RecruitBoardNearByCondition(
    Double latitude,
    Double longitude,
    Double radius,
    String keyword,
    Pageable pageable
) {

}
