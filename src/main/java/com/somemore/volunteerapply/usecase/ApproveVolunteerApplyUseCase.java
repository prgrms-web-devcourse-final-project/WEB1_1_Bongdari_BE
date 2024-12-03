package com.somemore.volunteerapply.usecase;


import java.util.UUID;

public interface ApproveVolunteerApplyUseCase {

    void approve(Long id, UUID centerId);

}
