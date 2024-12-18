package com.somemore.domains.community.service.comment;

import com.somemore.domains.community.domain.CommunityBoard;
import com.somemore.domains.community.domain.CommunityComment;
import com.somemore.domains.community.dto.response.CommunityCommentResponseDto;
import com.somemore.domains.community.repository.board.CommunityBoardRepository;
import com.somemore.domains.community.repository.comment.CommunityCommentRepository;
import com.somemore.domains.community.usecase.comment.DeleteCommunityCommentUseCase;
import com.somemore.domains.volunteer.domain.Volunteer;
import com.somemore.domains.volunteer.repository.VolunteerRepository;
import com.somemore.global.auth.oauth.OAuthProvider;
import com.somemore.support.IntegrationTestSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.UUID;

import static com.somemore.support.fixture.CommunityBoardFixture.createCommunityBoard;
import static com.somemore.support.fixture.CommunityCommentFixture.createCommunityComment;
import static org.assertj.core.api.Assertions.assertThat;

class CommunityCommentQueryServiceTest extends IntegrationTestSupport {
    @Autowired
    private CommunityCommentQueryService communityCommentQueryService;
    @Autowired
    private CommunityCommentRepository communityCommentRepository;
    @Autowired
    private CommunityBoardRepository communityBoardRepository;
    @Autowired
    private VolunteerRepository volunteerRepository;
    @Autowired
    private DeleteCommunityCommentUseCase deleteCommunityCommentUseCase;

    private Long boardId, commentId, replyId, commentId2;
    UUID writerId;

    @BeforeEach
    void setUp() {
        String oAuthId1 = "example-oauth-id";
        Volunteer volunteer1 = Volunteer.createDefault(OAuthProvider.NAVER, oAuthId1);
        volunteerRepository.save(volunteer1);
        writerId = volunteer1.getId();

        CommunityBoard communityBoard = createCommunityBoard(writerId);
        communityBoardRepository.save(communityBoard);
        boardId = communityBoard.getId();

        CommunityComment communityComment1 = createCommunityComment(boardId, writerId);
        communityCommentRepository.save(communityComment1);
        commentId = communityComment1.getId();

        CommunityComment communityComment2 = createCommunityComment(boardId, writerId);
        communityCommentRepository.save(communityComment2);
        commentId2 = communityComment2.getId();

        for (int i = 1; i <= 12; i++) {
            String content = "제목" + i;
            CommunityComment communityComment = createCommunityComment(content, boardId, writerId);
            communityCommentRepository.save(communityComment);
        }

        CommunityComment communityReply = createCommunityComment(boardId, writerId, commentId);
        communityCommentRepository.save(communityReply);
        replyId = communityReply.getId();
    }

    @AfterEach
    void tearDown() {
        communityCommentRepository.deleteAllInBatch();
    }

    @DisplayName("커뮤니티 게시글에 달린 댓글을 조회할 수 있다.")
    @Test
    void getCommentsByCommunityBoardId() {

        //given
        //when
        Page<CommunityCommentResponseDto> comments = communityCommentQueryService.getCommunityCommentsByBoardId(boardId, 1);

        //then
        assertThat(comments).isNotNull();
        assertThat(comments.getTotalElements()).isEqualTo(15);
        assertThat(comments.getSize()).isEqualTo(4);
        assertThat(comments.getTotalPages()).isEqualTo(4);
    }

    @DisplayName("삭제된 대댓글의 경우 조회할 수 없다.")
    @Test
    void doesNotFindReply() {

        //given
        deleteCommunityCommentUseCase.deleteCommunityComment(writerId, replyId, boardId);

        //when
        Page<CommunityCommentResponseDto> comments = communityCommentQueryService.getCommunityCommentsByBoardId(boardId, 0);

        //then
        assertThat(comments).isNotNull();
        assertThat(comments.getTotalElements()).isEqualTo(14);
        assertThat(comments.getSize()).isEqualTo(4);
    }

    @DisplayName("대댓글이 없는 삭제된 댓글의 경우 조회할 수 없다.")
    @Test
    void doesNotFindCommennt() {

        //given
        deleteCommunityCommentUseCase.deleteCommunityComment(writerId, commentId2, boardId);

        //when
        Page<CommunityCommentResponseDto> comments = communityCommentQueryService.getCommunityCommentsByBoardId(boardId, 0);

        //then
        assertThat(comments).isNotNull();
        assertThat(comments.getTotalElements()).isEqualTo(14);
        assertThat(comments.getSize()).isEqualTo(4);
    }

    @DisplayName("대댓글이 있는 댓글의 경우 삭제된 댓글로 조회할 수 있다.")
    @Test
    void getCommentsByCommunityBoardIdWithDeletedComment() {

        //given
        deleteCommunityCommentUseCase.deleteCommunityComment(writerId, commentId, boardId);

        //when
        Page<CommunityCommentResponseDto> comments = communityCommentQueryService.getCommunityCommentsByBoardId(boardId, 0);

        //then
        assertThat(comments).isNotNull();
        assertThat(comments.getTotalElements()).isEqualTo(15);
        assertThat(comments.getSize()).isEqualTo(4);
    }
}
