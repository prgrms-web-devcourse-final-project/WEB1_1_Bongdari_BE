package com.somemore.center.service;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_PREFER_ITEM;
import static com.somemore.global.exception.ExceptionMessage.UNAUTHORIZED_PREFER_ITEM;

import com.somemore.center.domain.PreferItem;
import com.somemore.center.repository.preferitem.PreferItemRepository;
import com.somemore.center.usecase.DeletePreferItemUseCase;
import com.somemore.global.exception.BadRequestException;
import com.somemore.global.exception.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class DeletePreferItemService implements DeletePreferItemUseCase {

    private final PreferItemRepository preferItemRepository;

    @Override
    public void deletePreferItem(UUID centerId, Long preferItemId) {
        PreferItem preferItem = preferItemRepository.findById(preferItemId)
                .orElseThrow(() -> new NoSuchElementException(NOT_EXISTS_PREFER_ITEM));

        validatePreferItemOwnership(centerId, preferItem);

        preferItemRepository.deleteById(preferItemId);
    }

    private void validatePreferItemOwnership(UUID centerId, PreferItem preferItem) {
        if (preferItem.getCenterId().equals(centerId)) {
            return;
        }
        throw new BadRequestException(UNAUTHORIZED_PREFER_ITEM);
    }
}
