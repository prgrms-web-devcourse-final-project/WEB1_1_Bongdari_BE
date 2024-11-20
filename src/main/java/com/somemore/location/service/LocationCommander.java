package com.somemore.location.service;

import com.somemore.location.domain.Location;
import com.somemore.location.dto.request.LocationCreateRequestDto;
import com.somemore.location.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class LocationCommander implements LocationCommandService {

    private final LocationRepository locationRepository;

    public Long createLocation(LocationCreateRequestDto dto) {
        Location location = dto.toEntity();

        locationRepository.save(location);

        return location.getId();
    }
}
