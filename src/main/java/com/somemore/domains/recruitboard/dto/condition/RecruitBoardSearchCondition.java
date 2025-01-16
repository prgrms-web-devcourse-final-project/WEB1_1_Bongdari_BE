package com.somemore.domains.recruitboard.dto.condition;

import com.somemore.domains.recruitboard.domain.RecruitStatus;
import com.somemore.domains.recruitboard.domain.VolunteerCategory;
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
