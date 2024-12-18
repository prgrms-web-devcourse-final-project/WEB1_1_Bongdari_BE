package com.somemore.domains.recruitboard.dto.condition;

import com.somemore.domains.recruitboard.domain.RecruitStatus;
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
