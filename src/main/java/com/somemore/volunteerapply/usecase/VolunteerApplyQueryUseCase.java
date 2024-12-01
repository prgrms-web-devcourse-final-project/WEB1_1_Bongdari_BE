package com.somemore.volunteerapply.usecase;

import com.somemore.volunteerapply.domain.VolunteerApply;
import java.util.List;
import java.util.UUID;

public interface VolunteerApplyQueryUseCase {
    List<UUID> getVolunteerIdsByRecruitIds(List<Long> recruitIds);
    VolunteerApply getByRecruitIdAndVolunteerId(Long recruitId, UUID volunteerId);
}
