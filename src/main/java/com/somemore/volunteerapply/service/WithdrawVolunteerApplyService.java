package com.somemore.volunteerapply.service;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_VOLUNTEER_APPLY;
import static com.somemore.global.exception.ExceptionMessage.UNAUTHORIZED_VOLUNTEER_APPLY;

import com.somemore.global.exception.BadRequestException;
import com.somemore.volunteerapply.domain.VolunteerApply;
import com.somemore.volunteerapply.repository.VolunteerApplyRepository;
import com.somemore.volunteerapply.usecase.WithdrawVolunteerApplyUseCase;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class WithdrawVolunteerApplyService implements WithdrawVolunteerApplyUseCase {

    private final VolunteerApplyRepository volunteerApplyRepository;

    @Override
    public void withdraw(Long id, UUID volunteerId) {
        VolunteerApply apply = getApply(id);
        validateVolunteerIdentity(apply, volunteerId);

        apply.markAsDeleted();
        volunteerApplyRepository.save(apply);
    }

    private VolunteerApply getApply(Long id) {
        return volunteerApplyRepository.findById(id).orElseThrow(
                () -> new BadRequestException(NOT_EXISTS_VOLUNTEER_APPLY)
        );
    }

    private void validateVolunteerIdentity(VolunteerApply apply, UUID volunteerId) {
        if (apply.isOwnApplication(volunteerId)) {
            return;
        }

        throw new BadRequestException(UNAUTHORIZED_VOLUNTEER_APPLY);
    }
}
