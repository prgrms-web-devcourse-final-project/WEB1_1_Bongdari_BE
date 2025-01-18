package com.somemore.volunteer.usecase;

import java.util.List;
import java.util.UUID;

public interface GetNicknamesByIdsUseCase {

    List<String> getNicknamesByIds(List<UUID> ids);
}
