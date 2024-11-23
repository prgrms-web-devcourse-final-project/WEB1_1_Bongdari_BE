package com.somemore.volunteer.usecase.query;

import java.util.UUID;

public interface FindVolunteerIdUseCase {
    UUID findVolunteerIdByOAuthId(String oAuthId);
}
