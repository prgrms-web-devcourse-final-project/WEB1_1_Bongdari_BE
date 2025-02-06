package com.somemore.domains.community.service.comment;

import com.somemore.domains.community.domain.CommunityBoard;
import com.somemore.domains.community.domain.CommunityComment;
import com.somemore.domains.community.dto.request.CommunityBoardCreateRequestDto;
import com.somemore.domains.community.dto.request.CommunityCommentCreateRequestDto;
import com.somemore.domains.community.dto.request.CommunityCommentUpdateRequestDto;
import com.somemore.domains.community.repository.board.CommunityBoardRepository;
import com.somemore.domains.community.repository.comment.CommunityCommentRepository;
import com.somemore.global.exception.BadRequestException;
import com.somemore.support.IntegrationTestSupport;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.UUID;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_COMMUNITY_BOARD;
import static com.somemore.global.exception.ExceptionMessage.UNAUTHORIZED_COMMUNITY_COMMENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class UpdateCommunityCommentServiceTest extends IntegrationTestSupport {

    @Autowired
    private UpdateCommunityCommentService updateCommunityCommentService;
    @Autowired
    private CommunityCommentRepository communityCommentRepository;
    @Autowired
    private CommunityBoardRepository communityBoardRepository;

    private UUID writerId;
    private Long commentId, boardId;
    private CommunityCommentUpdateRequestDto updateRequestDto;

    @BeforeEach
    void setUp() {
        CommunityBoardCreateRequestDto boardDto = CommunityBoardCreateRequestDto.builder()
                .title("커뮤니티 테스트 제목")
                .content("커뮤니티 테스트 내용")
                .build();

        writerId = UUID.randomUUID();

        CommunityBoard communityBoard = communityBoardRepository.save(boardDto.toEntity(writerId));
        boardId = communityBoard.getId();

        CommunityCommentCreateRequestDto commentDto = CommunityCommentCreateRequestDto.builder()
                .content("커뮤니티 댓글 테스트 내용")
                .parentCommentId(null)
                .build();

        CommunityComment communityComment = communityCommentRepository.save(commentDto.toEntity(writerId, boardId));

        commentId = communityComment.getId();

        updateRequestDto = CommunityCommentUpdateRequestDto.builder()
                .content("수정한 커뮤니티 댓글 내용")
                .build();
    }

    @AfterEach
    void tearDown() {
        communityCommentRepository.deleteAllInBatch();
    }

    @DisplayName("댓글을 수정한다.")
    @Test
    void updateCommunityComment() {

        //given
        //when
        updateCommunityCommentService.updateCommunityComment(updateRequestDto, commentId, writerId, boardId);

        //then
        Optional<CommunityComment> communityComment = communityCommentRepository.findById(commentId);
        assertThat(communityComment).isPresent();
        assertThat(communityComment.get().getId()).isEqualTo(commentId);
        assertThat(communityComment.get().getContent()).isEqualTo("수정한 커뮤니티 댓글 내용");
    }

    @DisplayName("작성자가 아닌 id로 댓글을 수정하고자 할 때 예외를 던진다.")
    @Test
    void updateCommunityCommentWithNotWriterId() {

        //given
        //when
        ThrowableAssert.ThrowingCallable callable = () ->
                updateCommunityCommentService.updateCommunityComment(updateRequestDto, commentId,
                        UUID.randomUUID(), boardId);

        //then
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(callable)
                .withMessage(UNAUTHORIZED_COMMUNITY_COMMENT.getMessage());
    }

    @DisplayName("삭제된 게시글의 댓글을 수정하고자 할 때 예외를 던진다.")
    @Test
    void updateCommunityCommentWithDeletedBoardId() {

        //given
        communityBoardRepository.deleteAllInBatch();
        //when
        ThrowableAssert.ThrowingCallable callable = () ->
                updateCommunityCommentService.updateCommunityComment(updateRequestDto, commentId,
                        writerId, boardId);

        //then
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(callable)
                .withMessage(NOT_EXISTS_COMMUNITY_BOARD.getMessage());
    }
}
