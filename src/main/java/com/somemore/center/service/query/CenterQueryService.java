package com.somemore.center.service.query;
import com.somemore.center.domain.Center;
import com.somemore.center.domain.PreferItem;
import com.somemore.center.dto.response.CenterProfileResponseDto;
import com.somemore.center.repository.CenterRepository;
import com.somemore.center.usecase.query.CenterQueryUseCase;
import com.somemore.center.usecase.query.PreferItemQueryUseCase;
import com.somemore.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_CENTER;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CenterQueryService implements CenterQueryUseCase {

    private final PreferItemQueryUseCase preferItemQueryUseCase;
    private final CenterRepository centerRepository;

    @Override
    public CenterProfileResponseDto getCenterProfileByCenterId(UUID centerId) {

        Center center = getCenterById(centerId);
        List<PreferItem> preferItems = preferItemQueryUseCase.getPreferItemsByCenterId(centerId);

        return CenterProfileResponseDto.of(center, preferItems);
    }

    @Override
    public void validateCenterExists(UUID id) {
        if (centerRepository.doesNotExistById(id)) {
            throw new BadRequestException(NOT_EXISTS_CENTER.getMessage());
        }
    }

    private Center getCenterById(UUID centerId) {
        return centerRepository.findCenterById(centerId)
                .orElseThrow(() -> new BadRequestException(NOT_EXISTS_CENTER.getMessage()));
    }

}
