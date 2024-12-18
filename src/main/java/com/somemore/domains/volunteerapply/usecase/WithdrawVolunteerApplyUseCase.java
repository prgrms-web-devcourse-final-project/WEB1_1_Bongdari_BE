package com.somemore.domains.volunteerapply.usecase;

import java.util.UUID;

public interface WithdrawVolunteerApplyUseCase {

    void withdraw(Long id, UUID volunteerId);

}
