package com.somemore.domains.volunteerapply.usecase;

import com.somemore.domains.volunteerapply.domain.VolunteerApply;
import com.somemore.domains.volunteerapply.dto.condition.VolunteerApplySearchCondition;
import com.somemore.domains.volunteerapply.dto.response.VolunteerApplyResponseDto;
import com.somemore.domains.volunteerapply.dto.response.VolunteerApplySummaryResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface VolunteerApplyQueryUseCase {

    VolunteerApply getById(Long id);

    VolunteerApply getByRecruitIdAndVolunteerId(Long recruitId, UUID volunteerId);

    VolunteerApplySummaryResponseDto getSummaryByRecruitId(Long recruitId);

    VolunteerApplyResponseDto getVolunteerApplyByRecruitIdAndVolunteerId(Long recruitId, UUID volunteerId);

    List<UUID> getVolunteerIdsByRecruitIds(List<Long> recruitIds);

    List<VolunteerApply> getAllByIds(List<Long> ids);

    Page<VolunteerApply> getAllByRecruitId(Long recruitId, VolunteerApplySearchCondition condition);

    Page<VolunteerApply> getAllByVolunteerId(UUID volunteerId, VolunteerApplySearchCondition condition);

}
