package com.somemore.domains.volunteerapply.usecase;


import java.util.UUID;

public interface ApproveVolunteerApplyUseCase {

    void approve(Long id, UUID centerId);

}
