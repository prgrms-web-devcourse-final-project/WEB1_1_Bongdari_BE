package com.somemore.community.service.board;

import com.somemore.IntegrationTestSupport;
import com.somemore.auth.oauth.OAuthProvider;
import com.somemore.center.repository.center.CenterRepository;
import com.somemore.community.domain.CommunityBoard;
import com.somemore.community.dto.response.CommunityBoardDetailResponseDto;
import com.somemore.community.dto.response.CommunityBoardResponseDto;
import com.somemore.community.repository.board.CommunityBoardRepository;
import com.somemore.community.usecase.board.CreateCommunityBoardUseCase;
import com.somemore.community.usecase.board.DeleteCommunityBoardUseCase;
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
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import static com.somemore.common.fixture.CommunityBoardFixture.createCommunityBoard;


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
    @Autowired
    private CommunityBoardDocumentService communityBoardDocumentService;

    private UUID writerId1;
    private Long communityId1;

    @BeforeEach
    void setUp() {
        String oAuthId = "example-oauth-id";
        Volunteer volunteer = Volunteer.createDefault(OAuthProvider.NAVER, oAuthId);
        volunteerRepository.save(volunteer);

        String oAuthId2 = "example-oauth-id";
        Volunteer volunteer2 = Volunteer.createDefault(OAuthProvider.NAVER, oAuthId2);
        volunteerRepository.save(volunteer2);

        writerId1 = volunteer.getId();
        UUID writerId2 = volunteer2.getId();

        CommunityBoard communityBoard1 = createCommunityBoard(writerId1);
        communityBoardRepository.save(communityBoard1);
        communityBoardRepository.save(createCommunityBoard(writerId2));

        communityId1 = communityBoard1.getId();

        for (int i = 1; i <= 11; i++) {
            String title = "제목" + i;
            CommunityBoard communityBoard = createCommunityBoard(title, writerId1);
            communityBoardRepository.save(communityBoard);
        }

        for (int i = 1; i <= 20; i++) {
            String title = "제목" + i;
            CommunityBoard communityBoard = createCommunityBoard(title, writerId2);
            communityBoardRepository.save(communityBoard);
        }
    }

    @AfterEach
    void tearDown() {
        communityBoardRepository.deleteAllInBatch();
    }

    @DisplayName("저장된 커뮤니티 게시글 리스트를 페이지로 조회한다.")
    @Test
    void getAllCommunityBoards() {

        //given
        //when
        Page<CommunityBoardResponseDto> dtos = communityBoardQueryService.getCommunityBoards(2);

        //then
        assertThat(dtos).isNotNull();
        assertThat(dtos.getContent()).isNotNull();
        assertThat(dtos.getTotalElements()).isEqualTo(33);
        assertThat(dtos.getSize()).isEqualTo(10);
        assertThat(dtos.getTotalPages()).isEqualTo(4);
    }

    @DisplayName("저장된 커뮤니티 게시글 리스트를 작성자자별로 페이지 조회한다.")
    @Test
    void getCommunityBoardsByWriter() {

        //given
        //when
        Page<CommunityBoardResponseDto> dtos = communityBoardQueryService.getCommunityBoardsByWriterId(writerId1, 1);

        //then
        assertThat(dtos).isNotNull();
        assertThat(dtos.getTotalElements()).isEqualTo(12);
        assertThat(dtos.getSize()).isEqualTo(10);
        assertThat(dtos.getTotalPages()).isEqualTo(2);
    }

    @DisplayName("커뮤니티 게시글의 상세 정보를 조회한다.")
    @Test
    void getCommunityBoardDetail() {

        //given
        //when
        CommunityBoardDetailResponseDto communityBoard = communityBoardQueryService.getCommunityBoardDetail(communityId1);

        //then
        assertThat(communityBoard).isNotNull();
        assertThat(communityBoard.id()).isEqualTo(communityId1);
        assertThat(communityBoard.title()).isEqualTo("테스트 커뮤니티 게시글 제목");
        assertThat(communityBoard.content()).isEqualTo("테스트 커뮤니티 게시글 내용");
        assertThat(communityBoard.writerId()).isEqualTo(writerId1);
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

    @DisplayName("게시글을 elastic search index에 저장한다. (service)")
    @Test
    void saveCommunityBoardDocuments() {
        //given
        List<CommunityBoard> communityBoards = new ArrayList<>();

        CommunityBoard communityBoard = createCommunityBoard("저장 잘 되나요?", "안녕하세요",  UUID.randomUUID());
        CommunityBoard savedBoard = communityBoardRepository.save(communityBoard);
        communityBoards.add(savedBoard);

        //when
        communityBoardDocumentService.saveCommunityBoardDocuments(communityBoards);

        //then
        Page<CommunityBoardResponseDto> dtos = communityBoardDocumentService.getCommunityBoardBySearch("", 0);

        assertThat(dtos).isNotNull();
        assertThat(dtos.getContent()).isNotNull();
        assertThat(dtos.getTotalElements()).isEqualTo(17);
        assertThat(dtos.getSize()).isEqualTo(10);
        assertThat(dtos.getTotalPages()).isEqualTo(2);

        communityBoardRepository.deleteDocument(savedBoard.getId());
    }
}

