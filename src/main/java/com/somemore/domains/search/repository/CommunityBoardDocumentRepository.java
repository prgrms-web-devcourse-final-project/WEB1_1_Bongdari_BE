package com.somemore.domains.search.repository;

import com.somemore.domains.search.domain.CommunityBoardDocument;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

@ConditionalOnProperty(name = "elastic.search.enabled", havingValue = "true")
public interface CommunityBoardDocumentRepository extends ElasticsearchRepository<CommunityBoardDocument, Long> {
    List<CommunityBoardDocument> findAll();
    @Query("{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"title^3\", \"content^2\", \"writerNickname\"]}}")
    List<CommunityBoardDocument> findDocumentsByTitleOrContentOrNicknameContaining(String keyword);
}
