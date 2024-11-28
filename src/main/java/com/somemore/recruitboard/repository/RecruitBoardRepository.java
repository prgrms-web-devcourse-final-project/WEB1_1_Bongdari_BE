package com.somemore.recruitboard.repository;

import com.somemore.recruitboard.domain.RecruitBoard;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecruitBoardRepository {

    RecruitBoard save(RecruitBoard recruitBoard);

    List<RecruitBoard> saveAll(List<RecruitBoard> recruitBoards);

    Optional<RecruitBoard> findById(Long id);

    List<RecruitBoard> findAll();

    Page<RecruitBoard> findAllWithPaging(Pageable pageable);

}
