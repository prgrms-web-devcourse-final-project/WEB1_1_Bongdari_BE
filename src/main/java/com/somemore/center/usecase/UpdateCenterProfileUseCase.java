package com.somemore.center.usecase;

import com.somemore.domains.center.dto.request.CenterProfileUpdateRequestDto;
import java.util.UUID;

public interface UpdateCenterProfileUseCase {

    void updateCenterProfile(UUID centerId, CenterProfileUpdateRequestDto requestDto,
            String imgUrl);
}
