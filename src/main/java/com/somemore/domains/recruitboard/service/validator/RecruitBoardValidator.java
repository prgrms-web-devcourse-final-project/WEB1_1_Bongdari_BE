package com.somemore.domains.recruitboard.service.validator;

import static com.somemore.global.exception.ExceptionMessage.INVALID_DEADLINE_RECRUIT_BOARD_UPDATE;
import static com.somemore.global.exception.ExceptionMessage.INVALID_RECRUIT_BOARD_STATUS_UPDATE;
import static com.somemore.global.exception.ExceptionMessage.INVALID_RECRUIT_BOARD_TIME;
import static com.somemore.global.exception.ExceptionMessage.INVALID_RECRUIT_BOARD_TIME_UPDATE;
import static com.somemore.global.exception.ExceptionMessage.UNAUTHORIZED_RECRUIT_BOARD;

import com.somemore.domains.recruitboard.domain.RecruitBoard;
import com.somemore.domains.recruitboard.domain.RecruitStatus;
import com.somemore.global.exception.BadRequestException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class RecruitBoardValidator {

    public void validateRecruitBoardTime(LocalDateTime start, LocalDateTime end) {
        if (end.isAfter(start)) {
            return;
        }

        throw new BadRequestException(INVALID_RECRUIT_BOARD_TIME);
    }

    public void validateUpdateRecruitBoardTime(LocalDateTime createdAt, LocalDateTime start,
            LocalDateTime end) {
        validateRecruitBoardTime(start, end);

        LocalDateTime oneDayAfterCreatedAt = createdAt.plusDays(1).truncatedTo(ChronoUnit.DAYS);
        if (start.isAfter(oneDayAfterCreatedAt)) {
            return;
        }

        throw new BadRequestException(INVALID_RECRUIT_BOARD_TIME_UPDATE);
    }

    public void validateWriter(RecruitBoard recruitBoard, UUID centerId) {
        if (recruitBoard.isWriter(centerId)) {
            return;
        }

        throw new BadRequestException(UNAUTHORIZED_RECRUIT_BOARD);
    }

    public void validateUpdatable(RecruitBoard recruitBoard, LocalDateTime current) {
        if (recruitBoard.isUpdatable(current)) {
            return;
        }

        throw new BadRequestException(INVALID_DEADLINE_RECRUIT_BOARD_UPDATE);
    }

    public void validateRecruitStatus(RecruitStatus newStatus) {
        if (newStatus.isChangeable()) {
            return;
        }
        throw new BadRequestException(INVALID_RECRUIT_BOARD_STATUS_UPDATE);
    }

}
