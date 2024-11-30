package com.somemore.community.repository;

import com.somemore.IntegrationTestSupport;
import com.somemore.auth.oauth.OAuthProvider;
import com.somemore.community.domain.CommunityBoard;
import com.somemore.community.domain.CommunityBoardView;
import com.somemore.community.repository.board.CommunityBoardRepository;
import com.somemore.volunteer.domain.Volunteer;
import com.somemore.volunteer.repository.VolunteerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class CommunityBoardRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private CommunityBoardRepository communityBoardRepository;
    @Autowired
    private VolunteerRepository volunteerRepository;

    @DisplayName("커뮤니티 게시글 id로 커뮤니티 상세 정보를 조회할 수 있다. (Repository)")
    @Test
    void getCommunityBoardById() {
        //given
        CommunityBoard communityBoard = CommunityBoard.builder()
                .title("테스트 커뮤니티 게시글 제목")
                .content("테스트 커뮤니티 게시글 내용")
                .imgUrl("http://community.example.com/123")
                .writerId(UUID.randomUUID())
                .build();

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
        CommunityBoard communityBoard = CommunityBoard.builder()
                .title("테스트 커뮤니티 게시글 제목")
                .content("테스트 커뮤니티 게시글 내용")
                .imgUrl("http://community.example.com/123")
                .writerId(UUID.randomUUID())
                .build();

        communityBoardRepository.save(communityBoard);
        communityBoardRepository.deleteAllInBatch();

        //when
        Optional<CommunityBoard> foundCommunityBoard = communityBoardRepository.findById(communityBoard.getId());

        //then
        assertThat(foundCommunityBoard).isEmpty();
    }

    @DisplayName("저장된 전체 커뮤니티 게시글을 목록으로 조회할 수 있다. (Repository)")
    @Test
    void getCommunityBoards() {
        //given
        String oAuthId = "example-oauth-id";
        Volunteer volunteer = Volunteer.createDefault(OAuthProvider.NAVER, oAuthId);
        volunteerRepository.save(volunteer);

        CommunityBoard communityBoard1 = CommunityBoard.builder()
                .title("테스트 커뮤니티 게시글 제목1")
                .content("테스트 커뮤니티 게시글 내용1")
                .imgUrl("http://community.example.com/123")
                .writerId(volunteer.getId())
                .build();

        CommunityBoard communityBoard2 = CommunityBoard.builder()
                .title("테스트 커뮤니티 게시글 제목2")
                .content("테스트 커뮤니티 게시글 내용2")
                .imgUrl("http://community.example.com/12")
                .writerId(volunteer.getId())
                .build();

        communityBoardRepository.save(communityBoard1);
        communityBoardRepository.save(communityBoard2);

        //when
        List<CommunityBoardView> communityBoards = communityBoardRepository.getCommunityBoards();

        //then
        assertThat(communityBoards).hasSize(2);
        assertThat(communityBoards.getFirst().communityBoard().getId()).isEqualTo(communityBoard2.getId());
        assertThat(communityBoards.getFirst().communityBoard().getTitle()).isEqualTo(communityBoard2.getTitle());
        assertThat(communityBoards.getFirst().writerNickname()).isEqualTo(volunteer.getNickname());
        assertThat(communityBoards.getFirst().communityBoard().getCreatedAt()).isEqualTo(communityBoard2.getCreatedAt());
        assertThat(communityBoards.getLast().communityBoard().getId()).isEqualTo(communityBoard1.getId());
        assertThat(communityBoards.getLast().communityBoard().getTitle()).isEqualTo(communityBoard1.getTitle());
        assertThat(communityBoards.getLast().writerNickname()).isEqualTo(volunteer.getNickname());
        assertThat(communityBoards.getLast().communityBoard().getCreatedAt()).isEqualTo(communityBoard1.getCreatedAt());
    }

    @DisplayName("저장된 커뮤니티 게시글 리스트를 작성자별로 조회할 수 있다. (Repository)")
    @Test
    void getCommunityBoardsByWriterId() {
        //given
        String oAuthId = "example-oauth-id";
        Volunteer volunteer = Volunteer.createDefault(OAuthProvider.NAVER, oAuthId);
        volunteerRepository.save(volunteer);

        CommunityBoard communityBoard1 = CommunityBoard.builder()
                .title("테스트 커뮤니티 게시글 제목1")
                .content("테스트 커뮤니티 게시글 내용1")
                .imgUrl("http://community.example.com/123")
                .writerId(volunteer.getId())
                .build();

        CommunityBoard communityBoard2 = CommunityBoard.builder()
                .title("테스트 커뮤니티 게시글 제목2")
                .content("테스트 커뮤니티 게시글 내용2")
                .imgUrl("http://community.example.com/12")
                .writerId(volunteer.getId())
                .build();

        CommunityBoard communityBoard3 = CommunityBoard.builder()
                .title("테스트 커뮤니티 게시글 제목3")
                .content("테스트 커뮤니티 게시글 내용3")
                .imgUrl("http://community.example.com/1")
                .writerId(UUID.randomUUID())
                .build();

        communityBoardRepository.save(communityBoard1);
        communityBoardRepository.save(communityBoard2);
        communityBoardRepository.save(communityBoard3);

        //when
        List<CommunityBoardView> communityBoards = communityBoardRepository.findByWriterId(volunteer.getId());

        //then
        assertThat(communityBoards).hasSize(2);
        assertThat(communityBoards.getFirst().communityBoard().getId()).isEqualTo(communityBoard2.getId());
        assertThat(communityBoards.getFirst().communityBoard().getTitle()).isEqualTo(communityBoard2.getTitle());
        assertThat(communityBoards.getFirst().writerNickname()).isEqualTo(volunteer.getNickname());
        assertThat(communityBoards.getFirst().communityBoard().getCreatedAt()).isEqualTo(communityBoard2.getCreatedAt());
        assertThat(communityBoards.getLast().communityBoard().getId()).isEqualTo(communityBoard1.getId());
        assertThat(communityBoards.getLast().communityBoard().getTitle()).isEqualTo(communityBoard1.getTitle());
        assertThat(communityBoards.getLast().writerNickname()).isEqualTo(volunteer.getNickname());
        assertThat(communityBoards.getLast().communityBoard().getCreatedAt()).isEqualTo(communityBoard1.getCreatedAt());
    }

    @DisplayName("게시글 id로 게시글이 존재하는지 확인할 수 있다.")
    @Test
    void existsById() {

        //given
        UUID writerId = UUID.randomUUID();

        CommunityBoard communityBoard = CommunityBoard.builder()
                .title("테스트 커뮤니티 게시글 제목")
                .content("테스트 커뮤니티 게시글 내용")
                .imgUrl("http://community.example.com/123")
                .writerId(writerId)
                .build();

        CommunityBoard savedComment = communityBoardRepository.save(communityBoard);

        //when
        boolean isExist = communityBoardRepository.existsById(savedComment.getId());

        //then
        assertThat(isExist).isTrue();
    }
}
