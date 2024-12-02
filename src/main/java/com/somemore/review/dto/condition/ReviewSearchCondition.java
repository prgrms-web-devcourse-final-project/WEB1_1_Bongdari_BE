package com.somemore.review.dto.condition;

import com.somemore.recruitboard.domain.VolunteerType;
import lombok.Builder;
import org.springframework.data.domain.Pageable;

@Builder
public record ReviewSearchCondition(
        VolunteerType volunteerType,
        Pageable pageable
) {

}
