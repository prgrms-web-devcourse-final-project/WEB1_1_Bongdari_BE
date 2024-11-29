package com.somemore.community.service.comment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.somemore.IntegrationTestSupport;
import com.somemore.community.domain.CommunityComment;
import com.somemore.community.dto.request.CommunityCommentCreateRequestDto;
import com.somemore.community.dto.request.CommunityCommentUpdateRequestDto;
import com.somemore.community.repository.comment.CommunityCommentRepository;
import com.somemore.community.usecase.comment.CreateCommunityCommentUseCase;
import com.somemore.global.exception.BadRequestException;
import com.somemore.global.exception.ExceptionMessage;
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
    private CreateCommunityCommentUseCase createCommunityCommentUseCase;
    @Autowired
    private CommunityCommentRepository communityCommentRepository;

    private UUID writerId;
    private Long commentId;

    @BeforeEach
    void setUp() {
        CommunityCommentCreateRequestDto dto = CommunityCommentCreateRequestDto.builder()
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

    @DisplayName("댓글을 수정한다.")
    @Test
    void updateCommunityComment() {
        //given
        CommunityCommentUpdateRequestDto dto = CommunityCommentUpdateRequestDto.builder()
                .content("수정한 커뮤니티 댓글 내용")
                .build();

        //when
        updateCommunityCommentService.updateCommunityComment(dto, commentId, writerId);

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
        CommunityCommentUpdateRequestDto dto = CommunityCommentUpdateRequestDto.builder()
                .content("수정한 커뮤니티 댓글 내용")
                .build();

        //when
        ThrowableAssert.ThrowingCallable callable = () -> updateCommunityCommentService.updateCommunityComment(dto, commentId, UUID.randomUUID());

        //then
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(callable)
                .withMessage(ExceptionMessage.UNAUTHORIZED_COMMUNITY_COMMENT.getMessage());
    }
}
