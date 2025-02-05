package com.somemore.domains.recruitboard.repository;

import com.somemore.domains.recruitboard.domain.RecruitBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecruitBoardJpaRepository extends JpaRepository<RecruitBoard, Long> {
    List<RecruitBoard> findAllByDeletedFalse();
}
