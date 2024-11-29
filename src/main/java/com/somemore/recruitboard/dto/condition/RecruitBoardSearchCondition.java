package com.somemore.recruitboard.dto.condition;

import com.somemore.recruitboard.domain.RecruitStatus;
import com.somemore.recruitboard.domain.VolunteerType;
import lombok.Builder;
import org.springframework.data.domain.Pageable;

@Builder
public record RecruitBoardSearchCondition(
    String keyword,
    VolunteerType type,
    String region,
    Boolean admitted,
    RecruitStatus status,
    Pageable pageable
) {

}
