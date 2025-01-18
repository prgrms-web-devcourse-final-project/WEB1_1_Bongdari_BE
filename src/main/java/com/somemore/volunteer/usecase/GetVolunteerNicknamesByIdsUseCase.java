package com.somemore.volunteer.usecase;

import com.somemore.volunteer.repository.record.VolunteerNickname;

import java.util.List;
import java.util.UUID;

public interface GetVolunteerNicknamesByIdsUseCase {

    List<VolunteerNickname> getNicknamesByIds(List<UUID> ids);
}
