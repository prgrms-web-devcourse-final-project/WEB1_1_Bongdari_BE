package com.somemore.community.service.query;

import com.somemore.IntegrationTestSupport;
import com.somemore.auth.oauth.OAuthProvider;
import com.somemore.center.domain.Center;
import com.somemore.center.repository.CenterRepository;
import com.somemore.community.domain.CommunityBoard;
import com.somemore.community.dto.request.CommunityBoardCreateRequestDto;
import com.somemore.community.dto.response.CommunityBoardGetDetailResponseDto;
import com.somemore.community.dto.response.CommunityBoardGetResponseDto;
import com.somemore.community.repository.CommunityBoardRepository;
import com.somemore.community.usecase.command.CreateCommunityBoardUseCase;
import com.somemore.global.exception.BadRequestException;
import com.somemore.global.exception.ExceptionMessage;
import com.somemore.volunteer.domain.Volunteer;
import com.somemore.volunteer.repository.VolunteerRepository;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

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
    CommunityBoardQueryService communityBoardQueryService;

    @AfterEach
    void tearDown() {
        communityBoardRepository.deleteAllInBatch();
    }

    @DisplayName("저장된 커뮤니티 게시글 리스트를 조회한다.")
    @Test
    void getAllCommunityBoards() {

        //given
        String oAuthId = "example-oauth-id";
        Volunteer volunteer = Volunteer.createDefault(OAuthProvider.NAVER, oAuthId);

        Volunteer savedVolunteer = volunteerRepository.save(volunteer);

        Center center = Center.create(
                "기본 기관 이름",
                "010-1234-5678",
                "http://example.com/image.jpg",
                "기관 소개 내용",
                "http://example.com",
                "account123",
                "password123"
        );
        Center savedCenter = centerRepository.save(center);

        CommunityBoardCreateRequestDto dto1 = CommunityBoardCreateRequestDto.builder()
                .title("커뮤니티 테스트 제목1")
                .content("커뮤니티 테스트 내용1")
                .build();

        CommunityBoardCreateRequestDto dto2 = CommunityBoardCreateRequestDto.builder()
                .title("커뮤니티 테스트 제목2")
                .content("커뮤니티 테스트 내용2")
                .build();


        String imgUrl1 = "https://image.test.url/123";

        Long communityId1 = createCommunityBoardUseCase.createCommunityBoard(dto1, savedCenter.getId(), null);
        Long communityId2 = createCommunityBoardUseCase.createCommunityBoard(dto2, savedVolunteer.getId(), imgUrl1);

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
        assertThat(board1.writerNickname()).isEqualTo(savedCenter.getName());
        assertThat(board1.title()).isEqualTo(dto1.title());
        assertThat(board1.createdAt()).isEqualTo(communityBoard1.get().getCreatedAt());

        CommunityBoardGetResponseDto board2 = dtos.getFirst();
        assertThat(board2.id()).isEqualTo(communityId2);
        assertThat(board2.writerNickname()).isEqualTo(savedVolunteer.getNickname());
        assertThat(board2.title()).isEqualTo(dto2.title());
        assertThat(board2.createdAt()).isEqualTo(communityBoard2.get().getCreatedAt());
    }

    @DisplayName("저장된 커뮤니티 게시글 리스트를 작성자자별로 조회한다.")
    @Test
    void getCommunityBoardsByWriter() {

        //given
        String oAuthId = "example-oauth-id";
        Volunteer volunteer = Volunteer.createDefault(OAuthProvider.NAVER, oAuthId);

        Volunteer savedVolunteer = volunteerRepository.save(volunteer);

        CommunityBoardCreateRequestDto dto1 = CommunityBoardCreateRequestDto.builder()
                .title("커뮤니티 테스트 제목1")
                .content("커뮤니티 테스트 내용1")
                .build();

        CommunityBoardCreateRequestDto dto2 = CommunityBoardCreateRequestDto.builder()
                .title("커뮤니티 테스트 제목2")
                .content("커뮤니티 테스트 내용2")
                .build();

        String imgUrl1 = "https://image.test.url/123";

        Long communityId1 = createCommunityBoardUseCase.createCommunityBoard(dto1, savedVolunteer.getId(), null);
        Long communityId2 = createCommunityBoardUseCase.createCommunityBoard(dto2, savedVolunteer.getId(), imgUrl1);

        //when
        List<CommunityBoardGetResponseDto> dtos = communityBoardQueryService.getCommunityBoardsByWriterId(volunteer.getId());

        //then
        Optional<CommunityBoard> communityBoard1 = communityBoardRepository.findById(communityId1);
        Optional<CommunityBoard> communityBoard2 = communityBoardRepository.findById(communityId2);

        assertThat(dtos)
                .isNotNull()
                .hasSize(2);

        CommunityBoardGetResponseDto board1 = dtos.getLast();
        assertThat(board1.id()).isEqualTo(communityId1);
        assertThat(board1.writerNickname()).isEqualTo(savedVolunteer.getNickname());
        assertThat(board1.title()).isEqualTo(dto1.title());
        assertThat(board1.createdAt()).isEqualTo(communityBoard1.get().getCreatedAt());

        CommunityBoardGetResponseDto board2 = dtos.getFirst();
        assertThat(board2.id()).isEqualTo(communityId2);
        assertThat(board2.writerNickname()).isEqualTo(savedVolunteer.getNickname());
        assertThat(board2.title()).isEqualTo(dto2.title());
        assertThat(board2.createdAt()).isEqualTo(communityBoard2.get().getCreatedAt());
    }

    @DisplayName("커뮤니티 게시글의 상세 정보를 조회한다.")
    @Test
    void getCommunityBoardDetail() {

        //given
        String oAuthId = "example-oauth-id";
        Volunteer volunteer = Volunteer.createDefault(OAuthProvider.NAVER, oAuthId);

        Volunteer savedVolunteer = volunteerRepository.save(volunteer);

        CommunityBoardCreateRequestDto dto1 = CommunityBoardCreateRequestDto.builder()
                .title("커뮤니티 테스트 제목")
                .content("커뮤니티 테스트 내용")
                .build();

        String imgUrl = "https://image.test.url/123";

        Long communityId1 = createCommunityBoardUseCase.createCommunityBoard(dto1, savedVolunteer.getId(), imgUrl);

        //when
        CommunityBoardGetDetailResponseDto communityBoard = communityBoardQueryService.getCommunityBoardDetail(communityId1);

        //then
        assertThat(communityBoard).isNotNull();
        assertThat(communityBoard.id()).isEqualTo(communityId1);
        assertThat(communityBoard.title()).isEqualTo("커뮤니티 테스트 제목");
        assertThat(communityBoard.content()).isEqualTo("커뮤니티 테스트 내용");
        assertThat(communityBoard.imageUrl()).isEqualTo("https://image.test.url/123");

        assertThat(communityBoard.writerDetailDto().id()).isEqualTo(savedVolunteer.getId());
        assertThat(communityBoard.writerDetailDto().name()).isEqualTo(savedVolunteer.getNickname());
        assertThat(communityBoard.writerDetailDto().imgUrl()).isEqualTo(savedVolunteer.getImgUrl());
        assertThat(communityBoard.writerDetailDto().tier()).isEqualTo(savedVolunteer.getTier());
    }

    @DisplayName("삭제된 커뮤니티 게시글의 상세 정보를 조회할 때 예외를 던진다.")
    @Test
    void getCommunityBoardDetailWithDeletedId() {

        //given
        String oAuthId = "example-oauth-id";
        Volunteer volunteer = Volunteer.createDefault(OAuthProvider.NAVER, oAuthId);

        Volunteer savedVolunteer = volunteerRepository.save(volunteer);

        CommunityBoardCreateRequestDto dto1 = CommunityBoardCreateRequestDto.builder()
                .title("커뮤니티 테스트 제목")
                .content("커뮤니티 테스트 내용")
                .build();

        String imgUrl = "https://image.test.url/123";

        Long communityId = createCommunityBoardUseCase.createCommunityBoard(dto1, savedVolunteer.getId(), imgUrl);

        communityBoardRepository.deleteAllInBatch();

        //when
        ThrowableAssert.ThrowingCallable callable = () -> communityBoardQueryService.getCommunityBoardDetail(communityId);

        //then
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(callable)
                .withMessage(ExceptionMessage.NOT_EXISTS_COMMUNITY_BOARD.getMessage());

    }
}

