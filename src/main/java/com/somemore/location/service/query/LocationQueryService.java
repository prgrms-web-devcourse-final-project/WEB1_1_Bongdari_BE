package com.somemore.location.service.query;

import com.somemore.global.exception.BadRequestException;
import com.somemore.location.domain.Location;
import com.somemore.location.repository.LocationRepository;
import com.somemore.location.usecase.query.LocationQueryUseCase;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class LocationQueryService implements LocationQueryUseCase {

    private final LocationRepository locationRepository;

    @Override
    public Optional<Location> findById(Long id) {
        return locationRepository.findById(id);
    }

}
