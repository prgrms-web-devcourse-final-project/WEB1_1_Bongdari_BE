package com.somemore.domains.community.repository;

import com.somemore.domains.community.domain.CommunityBoard;
import com.somemore.domains.community.domain.CommunityComment;
import com.somemore.domains.community.repository.board.CommunityBoardRepository;
import com.somemore.domains.community.repository.comment.CommunityCommentRepository;
import com.somemore.domains.community.repository.mapper.CommunityCommentView;
import com.somemore.domains.volunteer.domain.Volunteer;
import com.somemore.domains.volunteer.repository.old.VolunteerRepository;
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
import static com.somemore.support.fixture.CommunityCommentFixture.createCommunityComment;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class CommunityCommentRepositoryTest extends IntegrationTestSupport {

    @Autowired
    CommunityCommentRepository communityCommentRepository;
    @Autowired
    CommunityBoardRepository communityBoardRepository;
    @Autowired
    VolunteerRepository volunteerRepository;

    private Long boardId;
    private UUID writerId;
    private CommunityComment savedComment;

    @BeforeEach
    void setUp() {
        String oAuthId = "example-oauth-id";
        Volunteer volunteer = Volunteer.createDefault(OAuthProvider.NAVER, oAuthId);
        volunteerRepository.save(volunteer);
        writerId = volunteer.getId();

        CommunityBoard communityBoard = createCommunityBoard(writerId);
        communityBoardRepository.save(communityBoard);
        boardId = communityBoard.getId();

        CommunityComment communityComment = createCommunityComment(boardId, writerId);
        savedComment = communityCommentRepository.save(communityComment);
    }

    @DisplayName("커뮤니티 게시글에 댓글을 생성할 수 있다. (Repository)")
    @Test
    void createParentCommunityComment() {
        //given
        //when
        //then
        assertThat(savedComment.getWriterId()).isEqualTo(writerId);
        assertThat(savedComment.getContent()).isEqualTo("커뮤니티 댓글 테스트 내용");
        assertThat(savedComment.getParentCommentId()).isNull();
    }

    @DisplayName("댓글에 대댓글을 생성할 수 있다. (Repository)")
    @Test
    void createCommunityCommentReply() {
        //given
        CommunityComment communityCommentReply = createCommunityComment(boardId, writerId, savedComment.getId());

        //when
        CommunityComment savedCommentReply = communityCommentRepository.save(communityCommentReply);

        //then
        assertThat(savedCommentReply.getWriterId()).isEqualTo(writerId);
        assertThat(savedCommentReply.getContent()).isEqualTo("커뮤니티 댓글 테스트 내용");
        assertThat(savedCommentReply.getParentCommentId()).isEqualTo(savedComment.getId());
    }

    @DisplayName("댓글을 id로 조회할 수 있다. (Repository)")
    @Test
    void findCommunityCommentById() {
        //given
        //when
        Optional<CommunityComment> comment = communityCommentRepository.findById(savedComment.getId());

        //then
        assertThat(comment).isPresent();
        assertThat(comment.get().getWriterId()).isEqualTo(writerId);
        assertThat(comment.get().getContent()).isEqualTo("커뮤니티 댓글 테스트 내용");
        assertThat(comment.get().getParentCommentId()).isNull();
    }

    @DisplayName("댓글 id로 댓글이 존재하는지 확인할 수 있다.")
    @Test
    void existsById() {
        //given
        //when
        boolean isExist = communityCommentRepository.existsById(savedComment.getId());

        //then
        assertThat(isExist).isTrue();
    }

    @DisplayName("게시글 id로 게시글에 달린 댓글을 페이지로 조회할 수 있다. (Repository)")
    @Test
    void findCommunityCommentByBoardId() {
        //given
        for (int i = 1; i <= 8; i++) {
            String content = "제목" + i;
            CommunityComment communityComment = createCommunityComment(content, boardId, writerId);
            communityCommentRepository.save(communityComment);
        }

        Pageable pageable = getPageable();

        //when
        Page<CommunityCommentView> comments = communityCommentRepository.findCommentsByBoardId(boardId, pageable);

        //then
        assertThat(comments).isNotNull();
        assertThat(comments.getTotalElements()).isEqualTo(9);
        assertThat(comments.getSize()).isEqualTo(4);
        assertThat(comments.getNumber()).isZero();
    }

    private Pageable getPageable() {
        return PageRequest.of(0, 4);
    }
}
