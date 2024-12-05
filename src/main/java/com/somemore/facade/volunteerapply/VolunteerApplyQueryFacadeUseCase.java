package com.somemore.facade.volunteerapply;

import com.somemore.volunteerapply.dto.condition.VolunteerApplySearchCondition;
import com.somemore.volunteerapply.dto.response.VolunteerApplyRecruitInfoResponseDto;
import com.somemore.volunteerapply.dto.response.VolunteerApplyVolunteerInfoResponseDto;
import java.util.UUID;
import org.springframework.data.domain.Page;

public interface VolunteerApplyQueryFacadeUseCase {

    Page<VolunteerApplyVolunteerInfoResponseDto> getVolunteerAppliesByRecruitIdAndCenterId(
            Long recruitId,
            UUID centerId, VolunteerApplySearchCondition condition);

    Page<VolunteerApplyRecruitInfoResponseDto> getVolunteerAppliesByVolunteerId(UUID volunteerId,
            VolunteerApplySearchCondition condition);

}
