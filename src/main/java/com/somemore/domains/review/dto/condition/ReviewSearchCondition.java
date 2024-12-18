package com.somemore.domains.review.dto.condition;

import com.somemore.domains.recruitboard.domain.VolunteerCategory;
import lombok.Builder;
import org.springframework.data.domain.Pageable;

@Builder
public record ReviewSearchCondition(
        VolunteerCategory category,
        Pageable pageable
) {

}
