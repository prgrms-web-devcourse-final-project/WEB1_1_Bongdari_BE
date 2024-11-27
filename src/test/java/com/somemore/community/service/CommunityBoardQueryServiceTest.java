package com.somemore.community.service;

import com.somemore.IntegrationTestSupport;
import com.somemore.auth.oauth.OAuthProvider;
import com.somemore.center.repository.CenterRepository;
import com.somemore.community.domain.CommunityBoard;
import com.somemore.community.dto.request.CommunityBoardCreateRequestDto;
import com.somemore.community.dto.response.CommunityBoardGetDetailResponseDto;
import com.somemore.community.dto.response.CommunityBoardGetResponseDto;
import com.somemore.community.repository.CommunityBoardRepository;
import com.somemore.community.usecase.CreateCommunityBoardUseCase;
import com.somemore.community.usecase.DeleteCommunityBoardUseCase;
import com.somemore.global.exception.BadRequestException;
import com.somemore.global.exception.ExceptionMessage;
import com.somemore.volunteer.domain.Volunteer;
import com.somemore.volunteer.repository.VolunteerRepository;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class CommunityBoardQueryServiceTest extends IntegrationTestSupport {

    @Autowired
    CommunityBoardRepository communityBoardRepository;
    @Autowired
    VolunteerRepository volunteerRepository;
    @Autowired
    CenterRepository centerRepository;
    @Autowired
    CreateCommunityBoardUseCase createCommunityBoardUseCase;
    @Autowired
    DeleteCommunityBoardUseCase deleteCommunityBoardUseCase;
    @Autowired
    CommunityBoardQueryService communityBoardQueryService;

    private UUID writerId1, writerId2;
    private Long communityId1, communityId2;
    private String imgUrl, nickName1, nickName2;

    @BeforeEach
    void setUp() {
        CommunityBoardCreateRequestDto dto1 = CommunityBoardCreateRequestDto.builder()
                .title("커뮤니티 테스트 제목1")
                .content("커뮤니티 테스트 내용1")
                .build();

        CommunityBoardCreateRequestDto dto2 = CommunityBoardCreateRequestDto.builder()
                .title("커뮤니티 테스트 제목2")
                .content("커뮤니티 테스트 내용2")
                .build();

        String oAuthId = "example-oauth-id";
        Volunteer volunteer = Volunteer.createDefault(OAuthProvider.NAVER, oAuthId);

        volunteerRepository.save(volunteer);

        String oAuthId2 = "example-oauth-id";
        Volunteer volunteer2 = Volunteer.createDefault(OAuthProvider.NAVER, oAuthId2);

        volunteerRepository.save(volunteer2);

        writerId1 = volunteer.getId();
        writerId2 = volunteer2.getId();
        nickName1 = volunteer.getNickname();
        nickName2 = volunteer2.getNickname();
        imgUrl = "https://image.test.url/123";

        communityId1 = createCommunityBoardUseCase.createCommunityBoard(dto1, writerId1, imgUrl);
        communityId2 = createCommunityBoardUseCase.createCommunityBoard(dto2, writerId2, null);

    }

    @AfterEach
    void tearDown() {
        communityBoardRepository.deleteAllInBatch();
    }

    @DisplayName("저장된 커뮤니티 게시글 리스트를 조회한다.")
    @Test
    void getAllCommunityBoards() {

        //given
        // when
        List<CommunityBoardGetResponseDto> dtos = communityBoardQueryService.getCommunityBoards();

        // then
        Optional<CommunityBoard> communityBoard1 = communityBoardRepository.findById(communityId1);
        Optional<CommunityBoard> communityBoard2 = communityBoardRepository.findById(communityId2);

        assertThat(dtos)
                .isNotNull()
                .hasSize(2);

        CommunityBoardGetResponseDto board1 = dtos.getLast();
        assertThat(board1.id()).isEqualTo(communityId1);
        assertThat(board1.title()).isEqualTo(communityBoard1.get().getTitle());
        assertThat(board1.writerNickname()).isEqualTo(nickName1);
        assertThat(board1.createdAt()).isEqualTo(communityBoard1.get().getCreatedAt());

        CommunityBoardGetResponseDto board2 = dtos.getFirst();
        assertThat(board2.id()).isEqualTo(communityId2);
        assertThat(board2.title()).isEqualTo(communityBoard2.get().getTitle());
        assertThat(board2.writerNickname()).isEqualTo(nickName2);
        assertThat(board2.createdAt()).isEqualTo(communityBoard2.get().getCreatedAt());
    }

    @DisplayName("저장된 커뮤니티 게시글 리스트를 작성자자별로 조회한다.")
    @Test
    void getCommunityBoardsByWriter() {

        //given
        //when
        List<CommunityBoardGetResponseDto> dtos = communityBoardQueryService.getCommunityBoardsByWriterId(writerId1);

        //then
        Optional<CommunityBoard> communityBoard1 = communityBoardRepository.findById(communityId1);

        assertThat(dtos)
                .isNotNull()
                .hasSize(1);

        CommunityBoardGetResponseDto board1 = dtos.getFirst();
        assertThat(board1.id()).isEqualTo(communityId1);
        assertThat(board1.title()).isEqualTo(communityBoard1.get().getTitle());
        assertThat(board1.writerNickname()).isEqualTo(nickName1);
        assertThat(board1.createdAt()).isEqualTo(communityBoard1.get().getCreatedAt());
    }

    @DisplayName("커뮤니티 게시글의 상세 정보를 조회한다.")
    @Test
    void getCommunityBoardDetail() {

        //given
        //when
        CommunityBoardGetDetailResponseDto communityBoard = communityBoardQueryService.getCommunityBoardDetail(communityId1);

        //then
        assertThat(communityBoard).isNotNull();
        assertThat(communityBoard.id()).isEqualTo(communityId1);
        assertThat(communityBoard.title()).isEqualTo("커뮤니티 테스트 제목1");
        assertThat(communityBoard.content()).isEqualTo("커뮤니티 테스트 내용1");
        assertThat(communityBoard.writerId()).isEqualTo(writerId1);
        assertThat(communityBoard.imageUrl()).isEqualTo(imgUrl);
    }

    @DisplayName("삭제된 커뮤니티 게시글의 상세 정보를 조회할 때 예외를 던진다.")
    @Test
    void getCommunityBoardDetailWithDeletedId() {

        //given
        deleteCommunityBoardUseCase.deleteCommunityBoard(writerId1, communityId1);

        //when
        ThrowableAssert.ThrowingCallable callable = () -> communityBoardQueryService.getCommunityBoardDetail(communityId1);

        //then
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(callable)
                .withMessage(ExceptionMessage.NOT_EXISTS_COMMUNITY_BOARD.getMessage());

    }
}

