package com.somemore.domains.search.scheduler;

import com.somemore.domains.community.repository.board.CommunityBoardRepository;
import com.somemore.domains.community.repository.mapper.CommunityBoardView;
import com.somemore.domains.search.domain.CommunityBoardDocument;
import com.somemore.domains.search.repository.SearchBoardRepository;
import com.somemore.support.IntegrationTestSupport;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@EnabledIf(value = "${elastic.search.enabled}", loadContext = true)
class CommunityBoardUpdateSchedulerTest extends IntegrationTestSupport {

    @Autowired
    private SearchBoardRepository searchBoardRepository;

    @Autowired
    private CommunityBoardRepository communityBoardRepository;

    @DisplayName("매일 자정 elastic search index에 전체 mysql 데이터를 저장한다.")
    @Test
    void updateCommunityBoardDocuments() {
        // given
        Pageable pageable = getPageable();

        // when
        // then
        Page<CommunityBoardDocument> communityBoards = searchBoardRepository
                .findByCommunityBoardsContaining(null, pageable);

        Awaitility.await()
                .atMost(3, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    Page<CommunityBoardView> updatedBoards = communityBoardRepository
                            .findCommunityBoards(null, pageable);
                    assertThat(updatedBoards.getContent())
                            .usingRecursiveComparison()
                            .ignoringFields("updatedAt")
                            .isEqualTo(communityBoards.getContent());
                    assertThat(updatedBoards.getTotalElements()).isEqualTo(communityBoards.getTotalElements());
                    assertThat(updatedBoards.getSize()).isEqualTo(communityBoards.getSize());
                });
    }

    private Pageable getPageable() {
        return PageRequest.of(0, 10);
    }
}
