package com.somemore.domains.location.service.query;

import com.somemore.domains.location.domain.Location;
import com.somemore.domains.location.repository.LocationRepository;
import com.somemore.domains.location.usecase.query.LocationQueryUseCase;
import com.somemore.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_LOCATION;

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
