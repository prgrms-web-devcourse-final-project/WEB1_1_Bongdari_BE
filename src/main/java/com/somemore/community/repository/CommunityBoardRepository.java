package com.somemore.community.repository;

import com.somemore.community.domain.CommunityBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityBoardRepository extends JpaRepository<CommunityBoard, Long> {

}
