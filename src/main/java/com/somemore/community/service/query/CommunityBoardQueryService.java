package com.somemore.community.service.query;

import com.somemore.center.usecase.query.CenterQueryUseCase;
import com.somemore.community.domain.CommunityBoard;
import com.somemore.community.dto.response.CommunityBoardGetDetailResponseDto;
import com.somemore.community.dto.response.CommunityBoardGetResponseDto;
import com.somemore.community.dto.response.WriterDetailDto;
import com.somemore.community.repository.CommunityBoardRepository;
import com.somemore.community.usecase.query.CommunityBoardQueryUseCase;
import com.somemore.global.exception.BadRequestException;
import com.somemore.volunteer.dto.response.VolunteerForCommunityResponseDto;
import com.somemore.volunteer.usecase.query.FindVolunteerIdUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_COMMUNITY_BOARD;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommunityBoardQueryService implements CommunityBoardQueryUseCase {

    private final CommunityBoardRepository communityBoardRepository;
    private final CenterQueryUseCase centerQueryUseCase;
    private final FindVolunteerIdUseCase findVolunteerIdUseCase;

    @Override
    public List<CommunityBoardGetResponseDto> getCommunityBoards() {
        List<CommunityBoard> boards = communityBoardRepository.getCommunityBoards();

        return boards.stream()
                .map(board -> {
                    String writerNickname = getWriterNickname(board.getWriterId());
                    return CommunityBoardGetResponseDto.fromEntity(board, writerNickname);
                })
                .toList();
    }

    @Override
    public List<CommunityBoardGetResponseDto> getCommunityBoardsByWriterId(UUID writerId) {
        List<CommunityBoard> boards = communityBoardRepository.getCommunityBoardsByWriterId(writerId);
        String writerNickname = getWriterNickname(writerId);

        return boards.stream()
                .map(board -> CommunityBoardGetResponseDto.fromEntity(board, writerNickname))
                .toList();
    }

    @Override
    public CommunityBoardGetDetailResponseDto getCommunityBoardDetail(Long id) {
        CommunityBoard board = communityBoardRepository.getCommunityBoardWithId(id)
                .orElseThrow(() -> new BadRequestException(NOT_EXISTS_COMMUNITY_BOARD.getMessage()));

        return CommunityBoardGetDetailResponseDto.fromEntity(board, getWriterDetail(board.getWriterId()));
    }

    private String getWriterNickname(UUID writerId) {
        String nickname = findVolunteerIdUseCase.getNicknameById(writerId);

        if (nickname == null) {
            nickname = centerQueryUseCase.getNameById(writerId);
        }
        return nickname;
    }

    private WriterDetailDto getWriterDetail(UUID writerId) {
        VolunteerForCommunityResponseDto volunteer = findVolunteerIdUseCase.getVolunteerDetailForCommunity(writerId);

        if (volunteer == null) {
            return centerQueryUseCase.getCenterDetailForCommunity(writerId);
        }
        return volunteer;
    }
}