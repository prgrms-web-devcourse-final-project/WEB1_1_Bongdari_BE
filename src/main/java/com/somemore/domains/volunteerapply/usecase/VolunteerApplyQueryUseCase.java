package com.somemore.domains.volunteerapply.usecase;

import com.somemore.domains.volunteerapply.domain.VolunteerApply;
import com.somemore.domains.volunteerapply.dto.condition.VolunteerApplySearchCondition;
import com.somemore.domains.volunteerapply.dto.response.VolunteerApplySummaryResponseDto;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;

public interface VolunteerApplyQueryUseCase {

    VolunteerApply getById(Long id);

    VolunteerApply getByRecruitIdAndVolunteerId(Long recruitId, UUID volunteerId);

    Long getRecruitBoardIdById(Long id);

    VolunteerApplySummaryResponseDto getSummaryByRecruitId(Long recruitId);

    List<UUID> getVolunteerIdsByRecruitIds(List<Long> recruitIds);

    List<VolunteerApply> getAllByIds(List<Long> ids);

    Page<VolunteerApply> getAllByRecruitId(Long recruitId, VolunteerApplySearchCondition condition);

    Page<VolunteerApply> getAllByVolunteerId(UUID volunteerId,
            VolunteerApplySearchCondition condition);
}
