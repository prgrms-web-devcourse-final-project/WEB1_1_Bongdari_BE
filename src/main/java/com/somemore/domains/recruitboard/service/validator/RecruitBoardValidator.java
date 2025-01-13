package com.somemore.domains.recruitboard.service.validator;

import com.somemore.domains.recruitboard.domain.RecruitBoard;
import com.somemore.global.exception.BadRequestException;
import java.time.temporal.ChronoUnit;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.somemore.global.exception.ExceptionMessage.INVALID_RECRUIT_BOARD_TIME;
import static com.somemore.global.exception.ExceptionMessage.INVALID_RECRUIT_BOARD_TIME_UPDATE;
import static com.somemore.global.exception.ExceptionMessage.UNAUTHORIZED_RECRUIT_BOARD;

@Component
public class RecruitBoardValidator {

    public void validateRecruitBoardTime(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (endDateTime.isAfter(startDateTime)) {
            return;
        }

        throw new BadRequestException(INVALID_RECRUIT_BOARD_TIME);
    }

    public void validateUpdateRecruitBoardTime(LocalDateTime createdAt, LocalDateTime startDateTime,
            LocalDateTime endDateTime) {
        validateRecruitBoardTime(startDateTime, endDateTime);

        LocalDateTime oneDayAfterCreatedAt = createdAt.plusDays(1).truncatedTo(ChronoUnit.DAYS);
        if (startDateTime.isAfter(oneDayAfterCreatedAt)) {
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

}
