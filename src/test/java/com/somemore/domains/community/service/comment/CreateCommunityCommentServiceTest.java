package com.somemore.domains.community.service.comment;

import com.somemore.domains.community.domain.CommunityBoard;
import com.somemore.domains.community.domain.CommunityComment;
import com.somemore.domains.community.dto.request.CommunityBoardCreateRequestDto;
import com.somemore.domains.community.dto.request.CommunityCommentCreateRequestDto;
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
import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_COMMUNITY_COMMENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class CreateCommunityCommentServiceTest extends IntegrationTestSupport {

    @Autowired
    private CreateCommunityCommentService createCommunityCommentService;
    @Autowired
    private CommunityCommentRepository communityCommentRepository;
    @Autowired
    private CommunityBoardRepository communityBoardRepository;

    private UUID writerId;
    private Long boardId;

    @BeforeEach
    void setUp() {
        CommunityBoardCreateRequestDto boardDto = CommunityBoardCreateRequestDto.builder()
                .title("커뮤니티 테스트 제목")
                .content("커뮤니티 테스트 내용")
                .build();

        writerId = UUID.randomUUID();

        CommunityBoard communityBoard = communityBoardRepository.save(boardDto.toEntity(writerId, "https://test.image/123"));
        boardId = communityBoard.getId();
    }

    @AfterEach
    void tearDown() {
        communityCommentRepository.deleteAllInBatch();
    }

    @DisplayName("커뮤니티 게시글에 댓글을 등록한다.")
    @Test
    void createCommunityCommentWithDto() {

        //given
        CommunityCommentCreateRequestDto dto = CommunityCommentCreateRequestDto.builder()
                .content("커뮤니티 댓글 테스트 내용")
                .parentCommentId(null)
                .build();

        //when
        Long commentId = createCommunityCommentService.createCommunityComment(dto, writerId, boardId);

        //then
        Optional<CommunityComment> communityComment = communityCommentRepository.findById(commentId);

        assertThat(communityComment).isPresent();
        assertThat(communityComment.get().getId()).isEqualTo(commentId);
        assertThat(communityComment.get().getWriterId()).isEqualTo(writerId);
        assertThat(communityComment.get().getContent()).isEqualTo("커뮤니티 댓글 테스트 내용");
        assertThat(communityComment.get().getParentCommentId()).isNull();
    }

    @DisplayName("댓글에 대댓글을 등록한다.")
    @Test
    void createCommunityCommentRelyWithDto() {

        //given
        CommunityCommentCreateRequestDto commentDto = CommunityCommentCreateRequestDto.builder()
                .content("커뮤니티 댓글 테스트 내용")
                .parentCommentId(null)
                .build();

        Long commentId = createCommunityCommentService.createCommunityComment(commentDto, writerId, boardId);

        CommunityCommentCreateRequestDto replyDto = CommunityCommentCreateRequestDto.builder()
                .content("커뮤니티 대댓글 테스트 내용")
                .parentCommentId(commentId)
                .build();

        //when
        Long replyCommentId = createCommunityCommentService.createCommunityComment(replyDto, writerId, boardId);

        //then
        Optional<CommunityComment> communityCommentReply = communityCommentRepository.findById(replyCommentId);

        assertThat(communityCommentReply).isPresent();
        assertThat(communityCommentReply.get().getId()).isEqualTo(replyCommentId);
        assertThat(communityCommentReply.get().getWriterId()).isEqualTo(writerId);
        assertThat(communityCommentReply.get().getContent()).isEqualTo("커뮤니티 대댓글 테스트 내용");
        assertThat(communityCommentReply.get().getParentCommentId()).isEqualTo(commentId);
    }

    @DisplayName("삭제된 댓글이나 존재하지 않는 댓글에 대댓글을 등록할 때 예외를 던진다.")
    @Test
    void createCommunityCommentReplyWithDeletedParentId() {

        //given
        CommunityCommentCreateRequestDto replyDto = CommunityCommentCreateRequestDto.builder()
                .content("커뮤니티 대댓글 테스트 내용")
                .parentCommentId(2L)
                .build();

        //when
        ThrowableAssert.ThrowingCallable callable = () -> createCommunityCommentService.createCommunityComment(replyDto, UUID.randomUUID(), boardId);

        //then
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(callable)
                .withMessage(NOT_EXISTS_COMMUNITY_COMMENT.getMessage());
    }

    @DisplayName("삭제된 게시글에 댓글을 등록할 때 예외를 던진다.")
    @Test
    void createCommunityCommentWithDeletedBoardId() {

        //given
        CommunityCommentCreateRequestDto commentDto = CommunityCommentCreateRequestDto.builder()
                .content("커뮤니티 댓글 테스트 내용")
                .parentCommentId(null)
                .build();

        communityBoardRepository.deleteAllInBatch();

        //when
        ThrowableAssert.ThrowingCallable callable = () -> createCommunityCommentService.createCommunityComment(commentDto, UUID.randomUUID(), boardId);

        //then
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(callable)
                .withMessage(NOT_EXISTS_COMMUNITY_BOARD.getMessage());
    }
}
