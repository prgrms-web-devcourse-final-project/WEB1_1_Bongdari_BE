package com.somemore.center.usecase.command;

import com.somemore.center.dto.request.PreferItemCreateRequestDto;

public interface CreatePreferItemUseCase {

    void createPreferItem(PreferItemCreateRequestDto requestDto);
}
