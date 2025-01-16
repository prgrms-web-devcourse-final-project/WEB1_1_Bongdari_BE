package com.somemore.domains.center.service.command;

import com.somemore.domains.center.domain.PreferItem;
import com.somemore.domains.center.repository.preferitem.PreferItemRepository;
import com.somemore.domains.center.usecase.command.DeletePreferItemUseCase;
import com.somemore.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_PREFER_ITEM;
import static com.somemore.global.exception.ExceptionMessage.UNAUTHORIZED_PREFER_ITEM;

@RequiredArgsConstructor
@Service
public class DeletePreferItemService implements DeletePreferItemUseCase {

    private final PreferItemRepository preferItemRepository;

    @Override
    public void deletePreferItem(UUID centerId, Long preferItemId) {
        PreferItem preferItem = preferItemRepository.findById(preferItemId)
                .orElseThrow(() -> new BadRequestException(NOT_EXISTS_PREFER_ITEM));

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
