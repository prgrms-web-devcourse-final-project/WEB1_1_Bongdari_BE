package com.somemore.recruitboard.dto.condition;

import com.somemore.recruitboard.domain.RecruitStatus;
import lombok.Builder;
import org.springframework.data.domain.Pageable;

@Builder
public record RecruitBoardNearByCondition(
    Double latitude,
    Double longitude,
    Double radius,
    String keyword,
    RecruitStatus status,
    Pageable pageable
) {

}
