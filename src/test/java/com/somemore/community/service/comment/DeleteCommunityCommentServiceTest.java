package com.somemore.community.service.comment;

import com.somemore.IntegrationTestSupport;
import com.somemore.community.dto.request.CommunityBoardCreateRequestDto;
import com.somemore.community.dto.request.CommunityCommentCreateRequestDto;
import com.somemore.community.repository.comment.CommunityCommentRepository;
import com.somemore.community.usecase.board.CreateCommunityBoardUseCase;
import com.somemore.community.usecase.comment.CreateCommunityCommentUseCase;
import com.somemore.global.exception.BadRequestException;
import com.somemore.global.exception.ExceptionMessage;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class DeleteCommunityCommentServiceTest extends IntegrationTestSupport {

    @Autowired
    private DeleteCommunityCommentService deleteCommunityCommentService;
    @Autowired
    private CreateCommunityCommentUseCase createCommunityCommentUseCase;
    @Autowired
    private CommunityCommentRepository communityCommentRepository;
    @Autowired
    private CreateCommunityBoardUseCase createCommunityBoardUseCase;

    private UUID writerId;
    private Long commentId;

    @BeforeEach
    void setUp() {
        CommunityBoardCreateRequestDto boardDto = CommunityBoardCreateRequestDto.builder()
                .title("커뮤니티 테스트 제목")
                .content("커뮤니티 테스트 내용")
                .build();

        writerId = UUID.randomUUID();

        Long boardId = createCommunityBoardUseCase.createCommunityBoard(boardDto, writerId, null);

        CommunityCommentCreateRequestDto dto = CommunityCommentCreateRequestDto.builder()
                .communityBoardId(boardId)
                .content("커뮤니티 댓글 테스트 내용")
                .parentCommentId(null)
                .build();

        writerId = UUID.randomUUID();

        commentId = createCommunityCommentUseCase.createCommunityComment(dto, writerId);
    }

    @AfterEach
    void tearDown() {
        communityCommentRepository.deleteAllInBatch();
    }

    @DisplayName("댓글 id로 댓글을 삭제한다.")
    @Test
    void deleteCommunityCommentWithId() {
        //given
        //when
        deleteCommunityCommentService.deleteCommunityComment(writerId, commentId);

        //then
        assertThat(communityCommentRepository.existsById(commentId)).isFalse();
    }

    @DisplayName("삭제된 댓글의 id로 댓글을 삭제할 때 예외를 던진다.")
    @Test
    void deleteCommunityCommentWithDeletedId() {
        //given
        deleteCommunityCommentService.deleteCommunityComment(writerId, commentId);

        //when
        ThrowableAssert.ThrowingCallable callable = () -> deleteCommunityCommentService.deleteCommunityComment(writerId, commentId);

        //then
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(callable)
                .withMessage(ExceptionMessage.NOT_EXISTS_COMMUNITY_COMMENT.getMessage());
    }

    @DisplayName("작성자가 아닌 id로 댓글을 삭제하고자 할 때 예외를 던진다.")
    @Test
    void deleteCommunityCommentWithNotWriterId() {
        //given
        //when
        ThrowableAssert.ThrowingCallable callable = () -> deleteCommunityCommentService.deleteCommunityComment(UUID.randomUUID(), commentId);

        //then
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(callable)
                .withMessage(ExceptionMessage.UNAUTHORIZED_COMMUNITY_COMMENT.getMessage());
    }
}
