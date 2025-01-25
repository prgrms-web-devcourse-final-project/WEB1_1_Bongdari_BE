package com.somemore.user.usecase;

import com.somemore.user.dto.basicinfo.CenterBasicInfoRequestDto;
import com.somemore.user.dto.basicinfo.VolunteerBasicInfoRequestDto;

import java.util.UUID;

public interface UpdateBasicInfoUseCase {

    void update(UUID userId, VolunteerBasicInfoRequestDto volunteerBasicInfoRequestDto);

    void update(UUID userId, CenterBasicInfoRequestDto centerBasicInfoRequestDto);
}
