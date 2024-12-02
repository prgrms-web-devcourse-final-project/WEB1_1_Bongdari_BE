package com.somemore.community.service.comment;

import com.somemore.IntegrationTestSupport;
import com.somemore.auth.oauth.OAuthProvider;
import com.somemore.community.domain.CommunityBoard;
import com.somemore.community.domain.CommunityComment;
import com.somemore.community.dto.request.CommunityBoardCreateRequestDto;
import com.somemore.community.dto.request.CommunityCommentCreateRequestDto;
import com.somemore.community.dto.response.CommunityCommentResponseDto;
import com.somemore.community.repository.board.CommunityBoardRepository;
import com.somemore.community.repository.comment.CommunityCommentRepository;
import com.somemore.community.usecase.comment.DeleteCommunityCommentUseCase;
import com.somemore.volunteer.domain.Volunteer;
import com.somemore.volunteer.repository.VolunteerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

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

    private Long boardId, commentId, replyId;
    UUID writerId1, writerId2;

    @BeforeEach
    void setUp() {
        String oAuthId1 = "example-oauth-id";
        Volunteer volunteer1 = Volunteer.createDefault(OAuthProvider.NAVER, oAuthId1);
        volunteerRepository.save(volunteer1);
        writerId1 = volunteer1.getId();

        String oAuthId2 = "example-oauth-id";
        Volunteer volunteer2 = Volunteer.createDefault(OAuthProvider.NAVER, oAuthId2);
        volunteerRepository.save(volunteer2);
        writerId2 = volunteer2.getId();

        CommunityBoardCreateRequestDto boardDto = CommunityBoardCreateRequestDto.builder()
                .title("커뮤니티 테스트 제목")
                .content("커뮤니티 테스트 내용")
                .build();
        CommunityBoard communityBoard = communityBoardRepository.save(boardDto.toEntity(writerId1, "https://test.image/123"));
        boardId = communityBoard.getId();

        CommunityCommentCreateRequestDto dto1 = CommunityCommentCreateRequestDto.builder()
                .communityBoardId(boardId)
                .content("커뮤니티 댓글 테스트 내용")
                .parentCommentId(null)
                .build();
        CommunityComment communityComment = communityCommentRepository.save(dto1.toEntity(writerId2));
        commentId = communityComment.getId();

        CommunityCommentCreateRequestDto dto2 = CommunityCommentCreateRequestDto.builder()
                .communityBoardId(boardId)
                .content("커뮤니티 대댓글 테스트 내용")
                .parentCommentId(commentId)
                .build();
        CommunityComment communityReply = communityCommentRepository.save(dto2.toEntity(writerId1));
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
        List<CommunityCommentResponseDto> comments = communityCommentQueryService.getCommunityCommentsByBoardId(boardId);

        //then
        assertThat(comments).hasSize(1);
        assertThat(comments.getFirst().id()).isEqualTo(commentId);
        assertThat(comments.getFirst().replies()).hasSize(1);
        assertThat(comments.getFirst().replies().getFirst().id()).isEqualTo(replyId);
    }

    @DisplayName("삭제된 댓글의 경우 조회할 수 없다.")
    @Test
    void doesNotFind() {

        //given
        CommunityCommentCreateRequestDto dto = CommunityCommentCreateRequestDto.builder()
                .communityBoardId(boardId)
                .content("커뮤니티 댓글 테스트 내용")
                .parentCommentId(null)
                .build();
        CommunityComment communityComment = communityCommentRepository.save(dto.toEntity(writerId2));

        deleteCommunityCommentUseCase.deleteCommunityComment(writerId2, communityComment.getId());
        deleteCommunityCommentUseCase.deleteCommunityComment(writerId1, replyId);

        //when
        List<CommunityCommentResponseDto> comments = communityCommentQueryService.getCommunityCommentsByBoardId(boardId);

        //then
        assertThat(comments).hasSize(1);
        assertThat(comments.getFirst().id()).isEqualTo(commentId);
        assertThat(comments.getFirst().replies()).isEmpty();
    }

    @DisplayName("대댓글이 있는 댓글의 경우 삭제된 댓글로 조회할 수 있다.")
    @Test
    void getCommentsByCommunityBoardIdWithDeletedComment() {

        //given
        deleteCommunityCommentUseCase.deleteCommunityComment(writerId2, commentId);
        //when
        List<CommunityCommentResponseDto> comments = communityCommentQueryService.getCommunityCommentsByBoardId(boardId);

        //then
        assertThat(comments).hasSize(1);
        assertThat(comments.getFirst().content()).isEqualTo("삭제된 댓글입니다");
        assertThat(comments.getFirst().writerNickname()).isEmpty();
        assertThat(comments.getFirst().replies()).hasSize(1);
        assertThat(comments.getFirst().replies().getFirst().id()).isEqualTo(replyId);
    }
}
