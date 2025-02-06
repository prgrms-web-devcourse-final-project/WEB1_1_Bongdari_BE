package com.somemore.center.usecase;

import com.somemore.center.dto.request.PreferItemCreateRequestDto;
import com.somemore.center.dto.response.PreferItemCreateResponseDto;
import java.util.UUID;

public interface CreatePreferItemUseCase {

    PreferItemCreateResponseDto createPreferItem(UUID centerId,
            PreferItemCreateRequestDto requestDto);
}
