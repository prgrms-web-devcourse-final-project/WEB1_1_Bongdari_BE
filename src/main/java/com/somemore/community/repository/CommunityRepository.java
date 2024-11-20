package com.somemore.community.repository;

import com.somemore.community.domain.CommunityBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityRepository extends JpaRepository<CommunityBoard, Long> {

}
