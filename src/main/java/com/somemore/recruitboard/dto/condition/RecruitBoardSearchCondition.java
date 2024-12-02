package com.somemore.recruitboard.dto.condition;

import com.somemore.recruitboard.domain.RecruitStatus;
import com.somemore.recruitboard.domain.VolunteerCategory;
import lombok.Builder;
import org.springframework.data.domain.Pageable;

@Builder
public record RecruitBoardSearchCondition(
    String keyword,
    VolunteerCategory category,
    String region,
    Boolean admitted,
    RecruitStatus status,
    Pageable pageable
) {

}
