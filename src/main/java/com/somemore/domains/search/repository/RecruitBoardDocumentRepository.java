package com.somemore.domains.search.repository;

import com.somemore.domains.search.annotation.ConditionalOnElasticSearchEnabled;
import com.somemore.domains.search.domain.RecruitBoardDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

@ConditionalOnElasticSearchEnabled
public interface RecruitBoardDocumentRepository extends ElasticsearchRepository<RecruitBoardDocument, Long> {
}
