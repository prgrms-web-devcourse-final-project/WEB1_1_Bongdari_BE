package com.somemore.domains.recruitboard.usecase;

import com.somemore.domains.recruitboard.domain.RecruitStatus;
import com.somemore.domains.recruitboard.dto.request.RecruitBoardLocationUpdateRequestDto;
import com.somemore.domains.recruitboard.dto.request.RecruitBoardUpdateRequestDto;

import java.time.LocalDateTime;
import java.util.UUID;

public interface UpdateRecruitBoardUseCase {
    void updateRecruitBoard(RecruitBoardUpdateRequestDto requestDto, Long id, UUID centerId, String imgUrl);

    void updateRecruitBoardLocation(RecruitBoardLocationUpdateRequestDto requestDto, Long id, UUID centerId);

    void updateRecruitBoardStatus(RecruitStatus status, Long id, UUID centerId, LocalDateTime currentDateTime);
}