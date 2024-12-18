package com.somemore.domains.center.service.query;

import com.somemore.domains.center.repository.center.CenterRepository;
import com.somemore.domains.center.usecase.query.CenterSignUseCase;
import com.somemore.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_CENTER;

@Service
@Transactional
@RequiredArgsConstructor
public class CenterSignService implements CenterSignUseCase {

    private final CenterRepository centerRepository;

    @Override
    public UUID getIdByAccountId(String accountId) {
        UUID centerId = centerRepository.findIdByAccountId(accountId);
        if (centerId == null) {
            throw new BadRequestException(NOT_EXISTS_CENTER);
        }

        return centerId;
    }

    @Override
    public String getPasswordByAccountId(String accountId) {
        String password = centerRepository.findPasswordByAccountId(accountId);
        if (password == null) {
            throw new BadRequestException(NOT_EXISTS_CENTER);
        }

        return password;
    }
}
