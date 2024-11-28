package com.somemore.community.service.comment;

import com.somemore.IntegrationTestSupport;
import com.somemore.community.domain.CommunityComment;
import com.somemore.community.dto.request.CommunityCommentCreateRequestDto;
import com.somemore.community.repository.comment.CommunityCommentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateCommunityCommentServiceTest extends IntegrationTestSupport {

    @Autowired
    private CreateCommunityCommentService createCommunityCommentService;
    @Autowired
    private CommunityCommentRepository communityCommentRepository;

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
                .build();

        UUID writerId = UUID.randomUUID();

        //when
        Long commentId = createCommunityCommentService.CreateCommunityComment(dto, writerId, null);

        //then
        Optional<CommunityComment> communityComment = communityCommentRepository.findById(commentId);

        assertThat(communityComment).isPresent();
        assertThat(communityComment.get().getId()).isEqualTo(commentId);
        assertThat(communityComment.get().getWriterId()).isEqualTo(writerId);
        assertThat(communityComment.get().getContent()).isEqualTo("커뮤니티 댓글 테스트 내용");
        assertThat(communityComment.get().getParentCommentId()).isNull();
    }

    @DisplayName("커뮤니티 게시글에 대댓글을 등록한다.")
    @Test
    void createCommunityCommenReplytWithDto() {

        //given
        CommunityCommentCreateRequestDto dto = CommunityCommentCreateRequestDto.builder()
                .content("커뮤니티 댓글 테스트 내용")
                .build();

        UUID writerId = UUID.randomUUID();

        //when
        Long commentId = createCommunityCommentService.CreateCommunityComment(dto, writerId, 2L);

        //then
        Optional<CommunityComment> communityComment = communityCommentRepository.findById(commentId);

        assertThat(communityComment).isPresent();
        assertThat(communityComment.get().getId()).isEqualTo(commentId);
        assertThat(communityComment.get().getWriterId()).isEqualTo(writerId);
        assertThat(communityComment.get().getContent()).isEqualTo("커뮤니티 댓글 테스트 내용");
        assertThat(communityComment.get().getParentCommentId()).isEqualTo(2L);
    }
}
