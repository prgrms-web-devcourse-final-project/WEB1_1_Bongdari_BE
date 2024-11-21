package com.somemore.recruitboard.service.command;

import com.somemore.location.usecase.command.CreateLocationUseCase;
import com.somemore.recruitboard.domain.RecruitBoard;
import com.somemore.recruitboard.dto.request.RecruitBoardCreateRequestDto;
import com.somemore.recruitboard.repository.RecruitBoardRepository;
import com.somemore.recruitboard.usecase.command.CreateRecruitBoardUseCase;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Transactional
@Service
public class CreateRecruitBoardService implements CreateRecruitBoardUseCase {

    private final RecruitBoardRepository recruitBoardRepository;
    private final CreateLocationUseCase createLocationUseCase;

    @Override
    public Long createRecruitBoard(
        RecruitBoardCreateRequestDto requestDto,
        UUID centerId,
        String imgUrl
    ) {
        Long locationId = createLocationUseCase.createLocation(requestDto.location());
        RecruitBoard recruitBoard = requestDto.toEntity(centerId, locationId, imgUrl);

        recruitBoardRepository.save(recruitBoard);

        return recruitBoard.getId();
    }

}
