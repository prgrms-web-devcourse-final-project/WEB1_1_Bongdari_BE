//package com.somemore.community.repository.board;
//
//import com.somemore.community.domain.CommunityBoardDocument;
//import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
//
//public interface CommunityBoardDocumentRepository extends ElasticsearchRepository<CommunityBoardDocument, Long> {
////    List<CommunityBoardDocument> findAll();
////    @Query("{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"title\", \"content\"]}}")
////    List<CommunityBoardDocument> findIdsByTitleOrContentContaining(String keyword);
//}