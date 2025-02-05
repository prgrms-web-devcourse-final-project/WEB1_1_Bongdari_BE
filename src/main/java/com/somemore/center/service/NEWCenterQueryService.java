package com.somemore.center.service;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_CENTER;

import com.somemore.center.domain.NEWCenter;
import com.somemore.center.dto.response.CenterProfileResponseDto;
import com.somemore.center.repository.NEWCenterRepository;
import com.somemore.center.repository.record.CenterOverviewInfo;
import com.somemore.center.repository.record.CenterProfileDto;
import com.somemore.center.usecase.NEWCenterQueryUseCase;
import com.somemore.domains.center.dto.response.PreferItemResponseDto;
import com.somemore.domains.center.usecase.query.PreferItemQueryUseCase;
import com.somemore.global.exception.ExceptionMessage;
import com.somemore.global.exception.NoSuchElementException;
import com.somemore.user.repository.usercommonattribute.record.UserProfileDto;
import com.somemore.user.usecase.UserQueryUseCase;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NEWCenterQueryService implements NEWCenterQueryUseCase {

    private final UserQueryUseCase userQueryUseCase;
    private final PreferItemQueryUseCase preferItemQueryUseCase;
    private final NEWCenterRepository centerRepository;

    @Override
    public NEWCenter getByUserId(UUID userId) {
        return centerRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException(ExceptionMessage.NOT_EXISTS_CENTER));
    }

    @Override
    public UUID getIdByUserId(UUID userId) {
        return getByUserId(userId).getId();
    }

    @Override
    public CenterProfileResponseDto getCenterProfileById(UUID centerId) {

        CenterProfileDto centerProfileDto = centerRepository.findCenterProfileById(centerId)
                .orElseThrow(() -> new NoSuchElementException(ExceptionMessage.NOT_EXISTS_CENTER));

        UserProfileDto userProfileDto = userQueryUseCase.getUserProfileByUserId(
                centerProfileDto.userId());

        List<PreferItemResponseDto> preferItems = preferItemQueryUseCase.getPreferItemDtosByCenterId(
                centerProfileDto.id());

        return CenterProfileResponseDto.of(centerProfileDto, userProfileDto, preferItems);
    }

    @Override
    public void validateCenterExists(UUID id) {
        if (centerRepository.doesNotExistById(id)) {
            throw new NoSuchElementException(NOT_EXISTS_CENTER);
        }
    }

    @Override
    public List<CenterOverviewInfo> getCenterOverviewsByIds(List<UUID> ids) {
        return centerRepository.findOverviewInfosByIds(ids);
    }

}
