package com.somemore.domains.volunteerapply.usecase;

import com.somemore.domains.volunteerapply.dto.condition.VolunteerApplySearchCondition;
import com.somemore.domains.volunteerapply.dto.response.VolunteerApplyRecruitInfoResponseDto;
import com.somemore.domains.volunteerapply.dto.response.VolunteerApplyWithReviewStatusResponseDto;
import com.somemore.domains.volunteerapply.dto.response.VolunteerApplyVolunteerInfoResponseDto;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface VolunteerApplyQueryFacadeUseCase {

    VolunteerApplyWithReviewStatusResponseDto getVolunteerApplyByRecruitIdAndVolunteerId(Long recruitId, UUID volunteerId);

    Page<VolunteerApplyVolunteerInfoResponseDto> getVolunteerAppliesByRecruitIdAndCenterId(Long recruitId, UUID centerId, VolunteerApplySearchCondition condition);

    Page<VolunteerApplyRecruitInfoResponseDto> getVolunteerAppliesByVolunteerId(UUID volunteerId, VolunteerApplySearchCondition condition);

}
