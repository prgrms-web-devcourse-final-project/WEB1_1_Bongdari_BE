package com.somemore.location.service.command;

import com.somemore.location.domain.Location;
import com.somemore.location.dto.request.LocationCreateRequestDto;
import com.somemore.location.repository.LocationRepository;
import com.somemore.location.usecase.command.CreateLocationUseCase;
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
