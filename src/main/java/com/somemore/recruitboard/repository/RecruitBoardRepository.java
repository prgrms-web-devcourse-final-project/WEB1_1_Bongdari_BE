package com.somemore.recruitboard.repository;

import com.somemore.recruitboard.domain.RecruitBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitBoardRepository extends JpaRepository<RecruitBoard, Long> {

}
