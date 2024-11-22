package com.somemore.center.service.query;

import com.somemore.center.repository.CenterRepository;
import com.somemore.center.usecase.query.CenterQueryUseCase;
import com.somemore.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_CENTER;

@RequiredArgsConstructor
@Service
public class CenterQueryService implements CenterQueryUseCase {

    private final CenterRepository centerRepository;

    @Override
    public void validateCenterExists(UUID id) {

        if (centerRepository.doesNotExistById(id)) {
            throw new BadRequestException(NOT_EXISTS_CENTER.getMessage());
        }
    }

}
