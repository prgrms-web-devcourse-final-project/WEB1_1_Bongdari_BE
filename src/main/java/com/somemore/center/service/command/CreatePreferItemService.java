package com.somemore.center.service.command;

import com.somemore.center.dto.request.PreferItemCreateRequestDto;
import com.somemore.center.repository.PreferItemRepository;
import com.somemore.center.usecase.command.CreatePreferItemUseCase;
import com.somemore.center.usecase.query.CenterQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class CreatePreferItemService implements CreatePreferItemUseCase {

    private final CenterQueryUseCase centerQueryUseCase;
    private final PreferItemRepository preferItemRepository;

    @Override
    public void createPreferItem(PreferItemCreateRequestDto requestDto) {

        centerQueryUseCase.validateCenterExists(requestDto.centerId());

        preferItemRepository.save(requestDto.createPreferItem());
    }

}
