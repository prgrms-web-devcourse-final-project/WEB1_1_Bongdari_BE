package com.somemore.volunteerapply.usecase;

import com.somemore.volunteerapply.dto.condition.VolunteerApplySearchCondition;
import com.somemore.volunteerapply.dto.response.VolunteerApplyDetailResponseDto;
import java.util.UUID;
import org.springframework.data.domain.Page;

public interface VolunteerApplyQueryFacadeUseCase {

    Page<VolunteerApplyDetailResponseDto> getVolunteerAppliesByRecruitIdAndCenterId(Long recruitId,
            UUID centerId, VolunteerApplySearchCondition condition);

}
