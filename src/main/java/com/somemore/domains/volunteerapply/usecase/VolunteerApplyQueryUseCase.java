package com.somemore.domains.volunteerapply.usecase;

import com.somemore.domains.volunteerapply.dto.condition.VolunteerApplySearchCondition;
import com.somemore.domains.volunteerapply.dto.response.VolunteerApplyResponseDto;
import com.somemore.domains.volunteerapply.dto.response.VolunteerApplySummaryResponseDto;
import com.somemore.domains.volunteerapply.domain.VolunteerApply;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;

public interface VolunteerApplyQueryUseCase {

    List<UUID> getVolunteerIdsByRecruitIds(List<Long> recruitIds);

    VolunteerApply getByRecruitIdAndVolunteerId(Long recruitId, UUID volunteerId);

    VolunteerApplySummaryResponseDto getSummaryByRecruitId(Long recruitId);

    VolunteerApplyResponseDto getVolunteerApplyByRecruitIdAndVolunteerId(Long recruitId,
                                                                         UUID volunteerId);

    Page<VolunteerApply> getAllByRecruitId(Long recruitId, VolunteerApplySearchCondition condition);

    Page<VolunteerApply> getAllByVolunteerId(UUID volunteerId,
            VolunteerApplySearchCondition condition);

    List<VolunteerApply> getAllByIds(List<Long> ids);

}
