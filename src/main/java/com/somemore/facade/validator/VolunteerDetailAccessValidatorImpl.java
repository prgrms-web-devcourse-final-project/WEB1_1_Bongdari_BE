package com.somemore.facade.validator;

import com.somemore.global.exception.BadRequestException;
import com.somemore.recruitboard.usecase.query.RecruitBoardQueryUseCase;
import com.somemore.volunteerApply.usecase.VolunteerApplyQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static com.somemore.global.exception.ExceptionMessage.UNAUTHORIZED_VOLUNTEER_DETAIL;

@Component
@RequiredArgsConstructor
public class VolunteerDetailAccessValidatorImpl implements VolunteerDetailAccessValidator {

    private final RecruitBoardQueryUseCase recruitBoardQueryUseCase;
    private final VolunteerApplyQueryUseCase volunteerApplyQueryUseCase;

    /**
     * 기관 ID를 기반으로 완료되지 않은 모집글들의 ID를 조회하고,
     * 해당 모집글들에 연관된 봉사자들의 ID 목록에 타겟 봉사자 ID가 포함되어 있는지 검증.
     */
    public void validateByCenterId(UUID centerId, UUID targetVolunteerId) {
        List<Long> notCompletedIdsByCenterIds = recruitBoardQueryUseCase.getNotCompletedIdsByCenterIds(centerId);

        List<UUID> volunteerIdsByRecruitIds = volunteerApplyQueryUseCase.getVolunteerIdsByRecruitIds(notCompletedIdsByCenterIds);

        if (volunteerIdsByRecruitIds.stream()
                .anyMatch(volunteerId -> volunteerId.equals(targetVolunteerId))) {
            return;
        }

        throw new BadRequestException(UNAUTHORIZED_VOLUNTEER_DETAIL);
    }
}