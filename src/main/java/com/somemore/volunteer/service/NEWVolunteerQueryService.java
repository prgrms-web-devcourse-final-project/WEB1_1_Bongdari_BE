package com.somemore.volunteer.service;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_VOLUNTEER;

import com.somemore.global.exception.ExceptionMessage;
import com.somemore.global.exception.NoSuchElementException;
import com.somemore.volunteer.domain.NEWVolunteer;
import com.somemore.volunteer.repository.NEWVolunteerRepository;
import com.somemore.volunteer.repository.record.VolunteerNicknameAndId;
import com.somemore.volunteer.usecase.NEWVolunteerQueryUseCase;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NEWVolunteerQueryService implements NEWVolunteerQueryUseCase {

    private final NEWVolunteerRepository volunteerRepository;

    @Override
    public NEWVolunteer getById(UUID id) {
        return volunteerRepository.findById(id)
                .orElseThrow(
                        () -> new NoSuchElementException(ExceptionMessage.NOT_EXISTS_VOLUNTEER));
    }

    @Override
    public NEWVolunteer getByUserId(UUID userId) {
        return volunteerRepository.findByUserId(userId)
                .orElseThrow(
                        () -> new NoSuchElementException(ExceptionMessage.NOT_EXISTS_VOLUNTEER));
    }

    @Override
    public UUID getUserIdById(UUID id) {
        return getById(id).getUserId();
    }

    @Override
    public UUID getIdByUserId(UUID userId) {
        return getByUserId(userId).getId();
    }

    @Override
    public String getNicknameById(UUID id) {
        return volunteerRepository.findNicknameById(id)
                .orElseThrow(
                        () -> new NoSuchElementException(ExceptionMessage.NOT_EXISTS_VOLUNTEER));
    }

    @Override
    public List<VolunteerNicknameAndId> getVolunteerNicknameAndIdsByIds(List<UUID> ids) {
        return volunteerRepository.findVolunteerNicknameAndIdsByIds(ids);
    }

    @Override
    public void validateExistsById(UUID id) {
        if (volunteerRepository.doesNotExistById(id)) {
            throw new NoSuchElementException(NOT_EXISTS_VOLUNTEER);
        }
    }
}
