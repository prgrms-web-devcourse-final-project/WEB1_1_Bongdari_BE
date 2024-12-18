package com.somemore.domains.volunteerapply.dto.condition;

import com.somemore.domains.volunteerapply.domain.ApplyStatus;
import lombok.Builder;
import org.springframework.data.domain.Pageable;

@Builder
public record VolunteerApplySearchCondition(
        ApplyStatus status,
        Boolean attended,
        Pageable pageable

) {

}
