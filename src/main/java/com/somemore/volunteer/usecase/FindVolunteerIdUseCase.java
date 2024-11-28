package com.somemore.volunteer.usecase;

import java.util.UUID;

public interface FindVolunteerIdUseCase {
    UUID findVolunteerIdByOAuthId(String oAuthId);
    String getNicknameById(UUID id);
}
