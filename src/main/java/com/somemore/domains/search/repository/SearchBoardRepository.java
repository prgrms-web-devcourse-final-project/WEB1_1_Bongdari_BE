package com.somemore.domains.search.repository;

import com.somemore.domains.community.domain.CommunityBoard;
import com.somemore.domains.recruitboard.domain.RecruitBoard;
import com.somemore.domains.recruitboard.dto.condition.RecruitBoardNearByCondition;
import com.somemore.domains.recruitboard.dto.condition.RecruitBoardSearchCondition;
import com.somemore.domains.recruitboard.repository.mapper.RecruitBoardDetail;
import com.somemore.domains.recruitboard.repository.mapper.RecruitBoardWithCenter;
import com.somemore.domains.search.domain.CommunityBoardDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SearchBoardRepository {

    Page<RecruitBoardDetail> findAllNearbyWithKeyword(RecruitBoardNearByCondition condition);
    Page<RecruitBoardWithCenter> findByRecruitBoardsContaining(RecruitBoardSearchCondition condition);
    void saveRecruitBoardDocuments(List<RecruitBoard> recruitBoards);
    void deleteRecruitBoardDocument(Long id);


    Page<CommunityBoardDocument> findByCommunityBoardsContaining(String keyword, Pageable pageable);
    void saveCommunityBoardDocuments(List<CommunityBoard> communityBoards);
    void deleteCommunityBoardDocument(Long id);
}
