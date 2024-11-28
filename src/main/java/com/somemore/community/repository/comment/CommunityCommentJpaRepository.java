package com.somemore.community.repository.comment;

import com.somemore.community.domain.CommunityComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityCommentJpaRepository extends JpaRepository<CommunityComment, Long> {
}
