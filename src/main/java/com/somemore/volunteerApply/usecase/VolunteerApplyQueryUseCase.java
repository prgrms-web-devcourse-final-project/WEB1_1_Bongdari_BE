package com.somemore.volunteerApply.usecase;

import java.util.List;
import java.util.UUID;

public interface VolunteerApplyQueryUseCase {

    List<UUID> getVolunteerIdsByRecruitIds(List<Long> recruitIds);
}
