package com.somemore.domains.community.repository.board;

import com.somemore.domains.community.domain.CommunityBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityBoardJpaRepository extends JpaRepository<CommunityBoard, Long> {
    boolean existsByIdAndDeletedFalse(Long id);
}
