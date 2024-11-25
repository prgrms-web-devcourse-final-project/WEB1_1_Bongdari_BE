package com.somemore.center.service.query;

import com.somemore.center.domain.PreferItem;
import com.somemore.center.dto.response.PreferItemResponseDto;
import com.somemore.center.repository.PreferItemRepository;
import com.somemore.center.usecase.query.PreferItemQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PreferItemQueryService implements PreferItemQueryUseCase {

    private final PreferItemRepository preferItemRepository;

    @Override
    public List<PreferItemResponseDto> getPreferItemDtosByCenterId(UUID centerId) {
        List<PreferItem> preferItems = getPreferItemsByCenterId(centerId);
        return preferItemConvertToDtos(preferItems);
    }

    //프론트와 의논후 private으로 전환 예정
    @Override
    public List<PreferItem> getPreferItemsByCenterId(UUID centerId) {
       return preferItemRepository.findByCenterId(centerId);
    }

    private static List<PreferItemResponseDto> preferItemConvertToDtos(List<PreferItem> preferItems) {
        return preferItems.stream()
                .map(PreferItemResponseDto::from)
                .toList();
    }
}
