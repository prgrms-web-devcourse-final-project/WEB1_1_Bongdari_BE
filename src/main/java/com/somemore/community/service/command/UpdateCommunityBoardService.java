package com.somemore.community.service.command;

import com.somemore.community.domain.CommunityBoard;
import com.somemore.community.dto.request.CommunityBoardUpdateRequestDto;
import com.somemore.community.repository.CommunityBoardRepository;
import com.somemore.community.usecase.command.UpdateCommunityBoardUseCase;
import com.somemore.global.exception.BadRequestException;
import com.somemore.location.usecase.command.UpdateLocationUseCase;
import com.somemore.recruitboard.domain.RecruitBoard;
import com.somemore.recruitboard.domain.RecruitStatus;
import com.somemore.recruitboard.dto.request.RecruitBoardLocationUpdateRequestDto;
import com.somemore.recruitboard.dto.request.RecruitBoardUpdateRequestDto;
import com.somemore.recruitboard.repository.RecruitBoardRepository;
import com.somemore.recruitboard.usecase.command.UpdateRecruitBoardUseCase;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.somemore.global.exception.ExceptionMessage.*;


@RequiredArgsConstructor
@Transactional
@Service
public class UpdateCommunityBoardService implements UpdateCommunityBoardUseCase {

    private final CommunityBoardRepository communityBoardRepository;

    @Override
    public void updateCommunityBoard(CommunityBoardUpdateRequestDto requestDto, Long communityBoardId, UUID writerId, String imgUrl) {
        CommunityBoard communityBoard = getCommunityBoardById(communityBoardId);
        validateWriter(communityBoard, writerId);
        communityBoard.updateWith(requestDto, imgUrl);

        communityBoardRepository.save(communityBoard);
    }

    private CommunityBoard getCommunityBoardById(Long id) {
        return communityBoardRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(NOT_EXISTS_COMMUNITY_BOARD.getMessage()));
    }

    private void validateWriter(CommunityBoard communityBoard, UUID writerId) {
        if (communityBoard.isWriter(writerId)) {
            return;
        }

        throw new BadRequestException(UNAUTHORIZED_COMMUNITY_BOARD.getMessage());
    }
}
