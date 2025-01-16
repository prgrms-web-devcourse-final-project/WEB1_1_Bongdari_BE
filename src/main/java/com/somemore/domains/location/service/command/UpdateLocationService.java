package com.somemore.domains.location.service.command;

import com.somemore.domains.location.domain.Location;
import com.somemore.domains.location.dto.request.LocationUpdateRequestDto;
import com.somemore.domains.location.repository.LocationRepository;
import com.somemore.domains.location.usecase.command.UpdateLocationUseCase;
import com.somemore.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_LOCATION;

@RequiredArgsConstructor
@Transactional
@Service
public class UpdateLocationService implements UpdateLocationUseCase {

    private final LocationRepository locationRepository;

    @Override
    public void updateLocation(LocationUpdateRequestDto requestDto, Long locationId) {
        Location location = getLocation(locationId);
        location.updateWith(requestDto);
        locationRepository.save(location);
    }

    private Location getLocation(Long locationId) {
        return locationRepository.findById(locationId)
            .orElseThrow(() -> new BadRequestException(NOT_EXISTS_LOCATION.getMessage()));
    }
}
