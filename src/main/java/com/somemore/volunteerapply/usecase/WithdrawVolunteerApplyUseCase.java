package com.somemore.volunteerapply.usecase;

import java.util.UUID;

public interface WithdrawVolunteerApplyUseCase {

    void withdraw(Long id, UUID volunteerId);

}
