package com.somemore.domains.search.repository;

import com.somemore.domains.search.domain.RecruitBoardDocument;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

@ConditionalOnProperty(name = "elastic.search.enabled", havingValue = "true")
public interface RecruitBoardDocumentRepository extends ElasticsearchRepository<RecruitBoardDocument, Long> {
}
