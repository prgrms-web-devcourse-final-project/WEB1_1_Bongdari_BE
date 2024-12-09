package com.somemore.recruitboard.service.command;

import com.somemore.global.common.event.DomainEventSubType;
import com.somemore.global.common.event.ServerEventPublisher;
import com.somemore.global.common.event.ServerEventType;
import com.somemore.location.usecase.command.CreateLocationUseCase;
import com.somemore.recruitboard.domain.RecruitBoard;
import com.somemore.recruitboard.dto.request.RecruitBoardCreateRequestDto;
import com.somemore.recruitboard.event.CreateRecruitBoardEvent;
import com.somemore.recruitboard.repository.RecruitBoardRepository;
import com.somemore.recruitboard.usecase.command.CreateRecruitBoardUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@RequiredArgsConstructor
@Transactional
@Service
public class CreateRecruitBoardService implements CreateRecruitBoardUseCase {

    private final RecruitBoardRepository recruitBoardRepository;
    private final CreateLocationUseCase createLocationUseCase;
    private final ServerEventPublisher serverEventPublisher;

    @Override
    public Long createRecruitBoard(
            RecruitBoardCreateRequestDto requestDto,
            UUID centerId,
            String imgUrl
    ) {
        Long locationId = createLocationUseCase.createLocation(requestDto.location());
        RecruitBoard recruitBoard = requestDto.toEntity(centerId, locationId, imgUrl);

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
