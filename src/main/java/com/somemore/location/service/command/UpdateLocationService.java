package com.somemore.location.service.command;

import com.somemore.location.domain.Location;
import com.somemore.location.dto.request.LocationUpdateRequestDto;
import com.somemore.location.repository.LocationRepository;
import com.somemore.location.usecase.command.UpdateLocationUseCase;
import com.somemore.location.usecase.query.LocationQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class UpdateLocationService implements UpdateLocationUseCase {

    private final LocationQueryUseCase locationQueryUseCase;
    private final LocationRepository locationRepository;

    @Override
    public void updateLocation(LocationUpdateRequestDto requestDto, Long locationId) {
        Location location = locationQueryUseCase.findByIdOrThrow(locationId);
        location.updateWith(requestDto);
        locationRepository.save(location);
    }
}
