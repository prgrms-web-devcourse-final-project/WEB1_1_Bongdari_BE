package com.somemore.community.scheduler;

import com.somemore.IntegrationTestSupport;
import com.somemore.community.repository.board.CommunityBoardRepository;
import com.somemore.community.repository.mapper.CommunityBoardView;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class CommunityBoardUpdateSchedulerTest extends IntegrationTestSupport {

    @Autowired
    private CommunityBoardRepository communityBoardRepository;

    @DisplayName("매일 자정 elastic search index에 전체 mysql 데이터를 저장한다.")
    @Test
    void updateCommunityBoardDocuments() {
        // given
        Pageable pageable = getPageable();

        // when
        // then
        Page<CommunityBoardView> communityBoards = communityBoardRepository
                .findByCommunityBoardsContaining("", pageable);

        Awaitility.await()
                .atMost(3, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    Page<CommunityBoardView> updatedBoards = communityBoardRepository
                            .findCommunityBoards(pageable);
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
