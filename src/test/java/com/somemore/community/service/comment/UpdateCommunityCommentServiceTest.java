package com.somemore.community.service.comment;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_COMMUNITY_BOARD;
import static com.somemore.global.exception.ExceptionMessage.UNAUTHORIZED_COMMUNITY_COMMENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.somemore.IntegrationTestSupport;
import com.somemore.community.domain.CommunityBoard;
import com.somemore.community.domain.CommunityComment;
import com.somemore.community.dto.request.CommunityBoardCreateRequestDto;
import com.somemore.community.dto.request.CommunityCommentCreateRequestDto;
import com.somemore.community.dto.request.CommunityCommentUpdateRequestDto;
import com.somemore.community.repository.board.CommunityBoardRepository;
import com.somemore.community.repository.comment.CommunityCommentRepository;
import com.somemore.global.exception.BadRequestException;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.UUID;

class UpdateCommunityCommentServiceTest extends IntegrationTestSupport {

    @Autowired
    private UpdateCommunityCommentService updateCommunityCommentService;
    @Autowired
    private CommunityCommentRepository communityCommentRepository;
    @Autowired
    private CommunityBoardRepository communityBoardRepository;

    private UUID writerId;
    private Long commentId;
    private CommunityCommentUpdateRequestDto updateRequestDto;

    @BeforeEach
    void setUp() {
        CommunityBoardCreateRequestDto boardDto = CommunityBoardCreateRequestDto.builder()
                .title("커뮤니티 테스트 제목")
                .content("커뮤니티 테스트 내용")
                .build();

        writerId = UUID.randomUUID();

        CommunityBoard communityBoard = communityBoardRepository.save(boardDto.toEntity(writerId, "https://test.image/123"));

        CommunityCommentCreateRequestDto commentDto = CommunityCommentCreateRequestDto.builder()
                .communityBoardId(communityBoard.getId())
                .content("커뮤니티 댓글 테스트 내용")
                .parentCommentId(null)
                .build();

        CommunityComment communityComment = communityCommentRepository.save(commentDto.toEntity(writerId));

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
        updateCommunityCommentService.updateCommunityComment(updateRequestDto, commentId, writerId);

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
        ThrowableAssert.ThrowingCallable callable = () -> updateCommunityCommentService.updateCommunityComment(updateRequestDto, commentId, UUID.randomUUID());

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
        ThrowableAssert.ThrowingCallable callable = () -> updateCommunityCommentService.updateCommunityComment(updateRequestDto, commentId, writerId);

        //then
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(callable)
                .withMessage(NOT_EXISTS_COMMUNITY_BOARD.getMessage());
    }
}
