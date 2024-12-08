package com.somemore.center.service.command;

import com.somemore.center.domain.PreferItem;
import com.somemore.center.dto.request.PreferItemCreateRequestDto;
import com.somemore.center.dto.response.PreferItemCreateResponseDto;
import com.somemore.center.repository.preferitem.PreferItemRepository;
import com.somemore.center.usecase.command.CreatePreferItemUseCase;
import com.somemore.center.usecase.query.CenterQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Transactional
@Service
public class CreatePreferItemService implements CreatePreferItemUseCase {

    private final CenterQueryUseCase centerQueryUseCase;
    private final PreferItemRepository preferItemRepository;

    @Override
    public PreferItemCreateResponseDto createPreferItem(UUID userId, PreferItemCreateRequestDto requestDto) {

        centerQueryUseCase.validateCenterExists(userId);

        PreferItem preferItem = requestDto.toEntity(userId);

        preferItemRepository.save(preferItem);

        return PreferItemCreateResponseDto.from(preferItem);
    }

}
