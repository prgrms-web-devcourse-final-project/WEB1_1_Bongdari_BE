package com.somemore.domains.recruitboard.repository;

import com.somemore.domains.recruitboard.domain.RecruitBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitBoardJpaRepository extends JpaRepository<RecruitBoard, Long> {

}
