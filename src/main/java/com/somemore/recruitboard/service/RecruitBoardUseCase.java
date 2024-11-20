package com.somemore.recruitboard.service;

import com.somemore.location.service.LocationCommandService;
import com.somemore.recruitboard.dto.request.RecruitCreateRequestDto;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class RecruitBoardUseCase implements RecruitBoardUseCaseService {

    private final RecruitBoardCommandService recruitBoardCommandService;
    private final LocationCommandService locationCommandService;

    @Override
    public Long createRecruitBoard(
        RecruitCreateRequestDto requestDto,
        Long centerId,
        Optional<String> imgUrl) {

        Long locationId = locationCommandService.createLocation(requestDto.location());

        return recruitBoardCommandService.create(
            requestDto.toCommandRequest(centerId, locationId, imgUrl.orElse("")));

    }
}
