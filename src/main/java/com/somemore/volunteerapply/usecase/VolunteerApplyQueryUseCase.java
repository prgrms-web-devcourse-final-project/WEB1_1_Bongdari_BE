package com.somemore.volunteerapply.usecase;

import com.somemore.volunteerapply.domain.VolunteerApply;
import com.somemore.volunteerapply.dto.condition.VolunteerApplySearchCondition;
import com.somemore.volunteerapply.dto.response.VolunteerApplyResponseDto;
import com.somemore.volunteerapply.dto.response.VolunteerApplySummaryResponseDto;
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
}
