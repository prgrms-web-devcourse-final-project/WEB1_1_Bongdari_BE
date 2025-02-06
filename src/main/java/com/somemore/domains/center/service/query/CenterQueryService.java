package com.somemore.domains.center.service.query;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_CENTER;

import com.somemore.domains.center.repository.center.CenterRepository;
import com.somemore.domains.center.usecase.query.CenterQueryUseCase;
import com.somemore.global.exception.BadRequestException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CenterQueryService implements CenterQueryUseCase {

    private final CenterRepository centerRepository;

    @Override
    public void validateCenterExists(UUID id) {
        if (centerRepository.doesNotExistById(id)) {
            throw new BadRequestException(NOT_EXISTS_CENTER.getMessage());
        }
    }

    @Override
    public String getNameById(UUID id) {
        String name = centerRepository.findNameById(id);

        if (name == null || name.isBlank()) {
            throw new BadRequestException(NOT_EXISTS_CENTER);
        }

        return name;
    }

}
