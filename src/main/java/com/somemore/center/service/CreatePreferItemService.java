package com.somemore.center.service;

import com.somemore.center.domain.PreferItem;
import com.somemore.center.dto.request.PreferItemCreateRequestDto;
import com.somemore.center.dto.response.PreferItemCreateResponseDto;
import com.somemore.center.repository.preferitem.PreferItemRepository;
import com.somemore.center.usecase.CreatePreferItemUseCase;
import com.somemore.center.usecase.NEWCenterQueryUseCase;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class CreatePreferItemService implements CreatePreferItemUseCase {

    private final NEWCenterQueryUseCase centerQueryUseCase;
    private final PreferItemRepository preferItemRepository;

    @Override
    public PreferItemCreateResponseDto createPreferItem(UUID centerId,
            PreferItemCreateRequestDto requestDto) {

        centerQueryUseCase.validateCenterExists(centerId);

        PreferItem preferItem = requestDto.toEntity(centerId);

        preferItemRepository.save(preferItem);

        return PreferItemCreateResponseDto.from(preferItem);
    }

}
