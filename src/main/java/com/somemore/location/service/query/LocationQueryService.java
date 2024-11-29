package com.somemore.location.service.query;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_LOCATION;

import com.somemore.global.exception.BadRequestException;
import com.somemore.location.domain.Location;
import com.somemore.location.repository.LocationRepository;
import com.somemore.location.usecase.query.LocationQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class LocationQueryService implements LocationQueryUseCase {

    private final LocationRepository locationRepository;

    @Override
    public Location getById(Long id) {
        return getLocation(id);
    }

    private Location getLocation(Long id) {
        return locationRepository.findById(id).orElseThrow(
            () -> new BadRequestException(NOT_EXISTS_LOCATION.getMessage())
        );
    }

}
