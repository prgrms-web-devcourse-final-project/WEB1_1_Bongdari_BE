package com.somemore.domains.search.repository;

import com.somemore.domains.community.domain.CommunityBoard;
import com.somemore.domains.recruitboard.domain.RecruitBoard;
import com.somemore.domains.recruitboard.dto.condition.RecruitBoardNearByCondition;
import com.somemore.domains.recruitboard.dto.condition.RecruitBoardSearchCondition;
import com.somemore.domains.search.domain.CommunityBoardDocument;
import com.somemore.domains.search.domain.RecruitBoardDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface SearchBoardRepository {

    Page<RecruitBoardDocument> findByRecruitBoardsContaining(RecruitBoardSearchCondition condition);
    Page<RecruitBoardDocument> findAllNearbyWithKeyword(RecruitBoardNearByCondition condition);
    Page<RecruitBoardDocument> findAllByCenterIdWithKeyword(UUID centerId, RecruitBoardSearchCondition condition);
    void saveRecruitBoardDocuments(List<RecruitBoard> recruitBoards);
    void deleteRecruitBoardDocument(Long id);

    Page<CommunityBoardDocument> findByCommunityBoardsContaining(String keyword, Pageable pageable);
    void saveCommunityBoardDocuments(List<CommunityBoard> communityBoards);
    void deleteAllCommunityBoardDocument();
}
