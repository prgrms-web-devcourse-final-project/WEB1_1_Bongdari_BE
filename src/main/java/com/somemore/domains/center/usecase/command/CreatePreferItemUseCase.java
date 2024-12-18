package com.somemore.domains.center.usecase.command;

import com.somemore.domains.center.dto.request.PreferItemCreateRequestDto;
import com.somemore.domains.center.dto.response.PreferItemCreateResponseDto;

import java.util.UUID;

public interface CreatePreferItemUseCase {

    PreferItemCreateResponseDto createPreferItem(UUID userId, PreferItemCreateRequestDto requestDto);
}
