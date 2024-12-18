package com.somemore.domains.location.service.command;

import com.somemore.domains.location.dto.request.LocationCreateRequestDto;
import com.somemore.domains.location.repository.LocationRepository;
import com.somemore.domains.location.usecase.command.CreateLocationUseCase;
import com.somemore.domains.location.domain.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class CreateLocationService implements CreateLocationUseCase {

    private final LocationRepository locationRepository;

    @Override
    public Long createLocation(LocationCreateRequestDto requestDto) {
        Location location = requestDto.toEntity();
        locationRepository.save(location);
        return location.getId();
    }

}
