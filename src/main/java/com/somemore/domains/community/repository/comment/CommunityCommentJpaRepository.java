package com.somemore.domains.community.repository.comment;

import com.somemore.domains.community.domain.CommunityComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityCommentJpaRepository extends JpaRepository<CommunityComment, Long> {
}
