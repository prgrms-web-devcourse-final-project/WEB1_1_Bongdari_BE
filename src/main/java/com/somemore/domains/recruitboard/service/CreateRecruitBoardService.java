package com.somemore.domains.recruitboard.service;

import com.somemore.domains.location.usecase.command.CreateLocationUseCase;
import com.somemore.domains.recruitboard.domain.RecruitBoard;
import com.somemore.domains.recruitboard.dto.request.RecruitBoardCreateRequestDto;
import com.somemore.domains.recruitboard.event.CreateRecruitBoardEvent;
import com.somemore.domains.recruitboard.repository.RecruitBoardRepository;
import com.somemore.domains.recruitboard.service.validator.RecruitBoardValidator;
import com.somemore.domains.recruitboard.usecase.CreateRecruitBoardUseCase;
import com.somemore.global.common.event.DomainEventSubType;
import com.somemore.global.common.event.ServerEventPublisher;
import com.somemore.global.common.event.ServerEventType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@RequiredArgsConstructor
@Transactional
@Service
public class CreateRecruitBoardService implements CreateRecruitBoardUseCase {

    private final RecruitBoardRepository recruitBoardRepository;
    private final RecruitBoardValidator recruitBoardValidator;
    private final CreateLocationUseCase createLocationUseCase;
    private final ServerEventPublisher serverEventPublisher;

    @Override
    public Long createRecruitBoard(RecruitBoardCreateRequestDto dto, UUID centerId) {
        recruitBoardValidator.validateRecruitBoardTime(dto.volunteerStartDateTime(), dto.volunteerEndDateTime());

        Long locationId = createLocationUseCase.createLocation(dto.location());
        RecruitBoard recruitBoard = dto.toEntity(centerId, locationId);

        recruitBoardRepository.save(recruitBoard);

        publishCreateRecruitBoardEvent(centerId, recruitBoard);
        return recruitBoard.getId();
    }

    private void publishCreateRecruitBoardEvent(UUID centerId, RecruitBoard recruitBoard) {
        CreateRecruitBoardEvent event = CreateRecruitBoardEvent.builder()
                .type(ServerEventType.DOMAIN_EVENT)
                .subType(DomainEventSubType.CREATE_RECRUIT_BOARD)
                .centerId(centerId)
                .recruitBoardId(recruitBoard.getId())
                .build();

        serverEventPublisher.publish(event);
    }
}
