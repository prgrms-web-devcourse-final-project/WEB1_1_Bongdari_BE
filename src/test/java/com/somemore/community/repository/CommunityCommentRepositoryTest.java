package com.somemore.community.repository;

import com.somemore.IntegrationTestSupport;
import com.somemore.community.domain.CommunityComment;
import com.somemore.community.repository.comment.CommunityCommentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class CommunityCommentRepositoryTest extends IntegrationTestSupport {
    @Autowired
    CommunityCommentRepository communityCommentRepository;

    @DisplayName("커뮤니티 게시글에 댓글을 생성할 수 있다. (Repository)")
    @Test
    void createCommunityComment() {

        //given
        UUID writerId = UUID.randomUUID();

        CommunityComment communityComment = CommunityComment.builder()
                .writerId(writerId)
                .content("커뮤니티 댓글 테스트 내용")
                .parentCommentId(null)
                .build();

        //when
        CommunityComment savedComment = communityCommentRepository.save(communityComment);

        //then
        assertThat(savedComment.getWriterId()).isEqualTo(writerId);
        assertThat(savedComment.getContent()).isEqualTo("커뮤니티 댓글 테스트 내용");
        assertThat(savedComment.getParentCommentId()).isNull();
    }

    @DisplayName("댓글에 대댓글을 생성할 수 있다. (Repository)")
    @Test
    void createCommunityCommentReply() {

        //given
        UUID writerId = UUID.randomUUID();

        CommunityComment communityComment = CommunityComment.builder()
                .writerId(writerId)
                .content("커뮤니티 댓글 테스트 내용")
                .parentCommentId(1L)
                .build();

        //when
        CommunityComment savedComment = communityCommentRepository.save(communityComment);

        //then
        assertThat(savedComment.getWriterId()).isEqualTo(writerId);
        assertThat(savedComment.getContent()).isEqualTo("커뮤니티 댓글 테스트 내용");
        assertThat(savedComment.getParentCommentId()).isEqualTo(1L);
    }

    @DisplayName("댓글을 id로 조회할 수 있다. (Repository)")
    @Test
    void findCommunityCommentById() {

        //given
        UUID writerId = UUID.randomUUID();

        CommunityComment communityComment = CommunityComment.builder()
                .writerId(writerId)
                .content("커뮤니티 댓글 테스트 내용")
                .parentCommentId(null)
                .build();

        CommunityComment savedComment = communityCommentRepository.save(communityComment);

        //when
        Optional<CommunityComment> comment = communityCommentRepository.findById(savedComment.getId());

        //then
        assertThat(comment).isPresent();
        assertThat(comment.get().getWriterId()).isEqualTo(writerId);
        assertThat(comment.get().getContent()).isEqualTo("커뮤니티 댓글 테스트 내용");
        assertThat(comment.get().getParentCommentId()).isNull();
    }
}
