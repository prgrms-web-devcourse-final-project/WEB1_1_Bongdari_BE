package com.somemore.domains.search.scheduler;

import com.somemore.domains.recruitboard.dto.condition.RecruitBoardSearchCondition;
import com.somemore.domains.recruitboard.repository.RecruitBoardRepository;
import com.somemore.domains.recruitboard.repository.mapper.RecruitBoardWithCenter;
import com.somemore.domains.search.domain.RecruitBoardDocument;
import com.somemore.domains.search.repository.SearchBoardRepository;
import com.somemore.support.IntegrationTestSupport;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@EnabledIf(value = "${elastic.search.enabled}", loadContext = true)
class RecruitBoardUpdateSchedulerTest extends IntegrationTestSupport {

    @Autowired
    private SearchBoardRepository searchBoardRepository;

    @Autowired
    private RecruitBoardRepository recruitBoardRepository;

    @DisplayName("매일 자정 elastic search index에 전체 mysql 데이터를 저장한다.")
    @Test
    void updateRecruitBoardDocuments() {
        // given
        Pageable pageable = getPageable();
        RecruitBoardSearchCondition condition = RecruitBoardSearchCondition.builder()
                .keyword("")
                .pageable(pageable)
                .build();

        // when
        // then
        Page<RecruitBoardDocument> recruitBoards = searchBoardRepository
                .findByRecruitBoardsContaining(condition);

        Awaitility.await()
                .atMost(3, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    Page<RecruitBoardWithCenter> updatedBoards = recruitBoardRepository
                            .findAllWithCenter(condition);
                    assertThat(updatedBoards.getContent())
                            .usingRecursiveComparison()
                            .ignoringFields("updatedAt")
                            .isEqualTo(recruitBoards.getContent());
                    assertThat(updatedBoards.getTotalElements()).isEqualTo(recruitBoards.getTotalElements());
                    assertThat(updatedBoards.getSize()).isEqualTo(recruitBoards.getSize());
                });
    }

    private Pageable getPageable() {
        Sort sort = Sort.by(Sort.Order.desc("created_at"));
        return PageRequest.of(0, 5, sort);
    }
}