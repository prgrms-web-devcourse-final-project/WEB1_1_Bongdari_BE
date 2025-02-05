package com.somemore.domains.community.repository;

import com.somemore.domains.community.domain.CommunityBoard;
import com.somemore.domains.community.repository.board.CommunityBoardRepository;
import com.somemore.domains.community.repository.mapper.CommunityBoardView;
import com.somemore.domains.volunteer.domain.Volunteer;
import com.somemore.domains.volunteer.repository.VolunteerRepository;
import com.somemore.global.auth.oauth.domain.OAuthProvider;
import com.somemore.support.IntegrationTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static com.somemore.support.fixture.CommunityBoardFixture.createCommunityBoard;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class CommunityBoardRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private CommunityBoardRepository communityBoardRepository;
    @Autowired
    private VolunteerRepository volunteerRepository;

    private UUID writerId;

    @BeforeEach
    void setUp() {
        String oAuthId1 = "example-oauth-id1";
        Volunteer volunteer1 = Volunteer.createDefault(OAuthProvider.NAVER, oAuthId1);
        volunteerRepository.save(volunteer1);
        writerId = volunteer1.getId();

        String oAuthId2 = "example-oauth-id2";
        Volunteer volunteer2 = Volunteer.createDefault(OAuthProvider.NAVER, oAuthId2);
        volunteerRepository.save(volunteer2);

        for (int i = 1; i <= 8; i++) {
            String title = "제목" + i;
            CommunityBoard communityBoard = createCommunityBoard(title, writerId);
            communityBoardRepository.save(communityBoard);
        }

        for (int i = 1; i <= 20; i++) {
            String title = "제목" + i;
            CommunityBoard communityBoard = createCommunityBoard(title, volunteer2.getId());
            communityBoardRepository.save(communityBoard);
        }
    }

    @DisplayName("커뮤니티 게시글 id로 커뮤니티 상세 정보를 조회할 수 있다. (Repository)")
    @Test
    void getCommunityBoardById() {
        //given
        CommunityBoard communityBoard = createCommunityBoard(UUID.randomUUID());
        communityBoardRepository.save(communityBoard);

        //when
        Optional<CommunityBoard> foundCommunityBoard = communityBoardRepository.findById(communityBoard.getId());

        //then
        assertThat(foundCommunityBoard).isNotNull();
        assertThat(foundCommunityBoard.get().getTitle()).isEqualTo("테스트 커뮤니티 게시글 제목");
        assertThat(foundCommunityBoard.get().getContent()).isEqualTo("테스트 커뮤니티 게시글 내용");
        assertThat(foundCommunityBoard.get().getImgUrl()).isEqualTo("http://community.example.com/123");
        assertThat(foundCommunityBoard.get().getWriterId()).isEqualTo(communityBoard.getWriterId());
    }

    @DisplayName("삭제된 커뮤니티 id로 커뮤니티 상세 정보를 조회할 때 예외를 반환할 수 있다. (Repository)")
    @Test
    void getCommunityBoardByDeletedId() {
        //given
        CommunityBoard communityBoard = createCommunityBoard(UUID.randomUUID());
        communityBoardRepository.save(communityBoard);

        communityBoardRepository.deleteAllInBatch();

        //when
        Optional<CommunityBoard> foundCommunityBoard = communityBoardRepository.findById(communityBoard.getId());

        //then
        assertThat(foundCommunityBoard).isEmpty();
    }

    @DisplayName("저장된 전체 커뮤니티 게시글을 페이지로 조회할 수 있다. (Repository)")
    @Test
    void getCommunityBoards() {

        //given
        Pageable pageable = getPageable();

        //when
        Page<CommunityBoardView> communityBoards = communityBoardRepository.findCommunityBoards(null, pageable);

        //then
        assertThat(communityBoards).isNotNull();
        assertThat(communityBoards.getTotalElements()).isEqualTo(28);
        assertThat(communityBoards.getSize()).isEqualTo(10);
        assertThat(communityBoards.getNumber()).isZero();
    }

    @DisplayName("저장된 커뮤니티 게시글 리스트를 작성자별로 조회할 수 있다. (Repository)")
    @Test
    void getCommunityBoardsByWriterId() {
        //given
        Pageable pageable = getPageable();

        //when
        Page<CommunityBoardView> communityBoards = communityBoardRepository.findByWriterId(writerId, pageable);

        //then
        assertThat(communityBoards).isNotNull();
        assertThat(communityBoards.getTotalElements()).isEqualTo(8);
        assertThat(communityBoards.getSize()).isEqualTo(10);
        assertThat(communityBoards.getNumber()).isZero();
    }

    @DisplayName("게시글 id로 게시글이 존재하는지 확인할 수 있다.")
    @Test
    void existsById() {
        //given
        CommunityBoard communityBoard = createCommunityBoard();
        communityBoardRepository.save(communityBoard);

        //when
        boolean isExist = communityBoardRepository.existsById(communityBoard.getId());

        //then
        assertThat(isExist).isTrue();
    }

    @DisplayName("검색 키워드가 포함된 게시글을 페이지로 조회할 수 있다. (rdb)")
    @Test
    void findByCommunityBoardsContaining() {

        //given
        Pageable pageable = getPageable();

        String title = "봉사";
        CommunityBoard communityBoard = createCommunityBoard(title, writerId);
        communityBoardRepository.save(communityBoard);

        //when
        Page<CommunityBoardView> communityBoards = communityBoardRepository.findCommunityBoards("봉사", pageable);

        //then
        assertThat(communityBoards).isNotNull();
        assertThat(communityBoards.getTotalElements()).isEqualTo(1);
        assertThat(communityBoards.getSize()).isEqualTo(10);
        assertThat(communityBoards.getNumber()).isZero();
    }

    private Pageable getPageable() {
        return PageRequest.of(0, 10);
    }
}
