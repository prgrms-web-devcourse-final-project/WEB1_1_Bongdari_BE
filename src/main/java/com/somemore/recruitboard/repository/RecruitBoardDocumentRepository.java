package com.somemore.recruitboard.repository;

import com.somemore.recruitboard.domain.RecruitBoardDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface RecruitBoardDocumentRepository extends ElasticsearchRepository<RecruitBoardDocument, Long> {
    List<RecruitBoardDocument> findAll();
    @Query("{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"title\", \"content\"]}}")
    List<RecruitBoardDocument> findIdsByTitleOrContentContaining(String keyword);
}
