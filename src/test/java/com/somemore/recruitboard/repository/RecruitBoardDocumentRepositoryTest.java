package com.somemore.recruitboard.repository;

import com.somemore.IntegrationTestSupport;
import com.somemore.center.domain.Center;
import com.somemore.center.repository.center.CenterRepository;
import com.somemore.location.domain.Location;
import com.somemore.location.repository.LocationRepository;
import com.somemore.recruitboard.domain.RecruitBoard;
import com.somemore.recruitboard.dto.condition.RecruitBoardSearchCondition;
import com.somemore.recruitboard.repository.mapper.RecruitBoardWithCenter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.somemore.common.fixture.CenterFixture.createCenter;
import static com.somemore.common.fixture.LocationFixture.createLocation;
import static com.somemore.common.fixture.RecruitBoardFixture.createRecruitBoard;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
public class RecruitBoardDocumentRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private RecruitBoardRepository recruitBoardRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private CenterRepository centerRepository;

    private final List<RecruitBoard> boards = new ArrayList<>();

    @BeforeEach
    void setUp() {
        Location location = createLocation();
        locationRepository.save(location);

        Center center = createCenter();
        centerRepository.save(center);

        for (int i = 1; i <= 30; i++) {
            String title = "제목" + i;
            RecruitBoard board = createRecruitBoard(title, center.getId(), location.getId());
            boards.add(board);
        }
        recruitBoardRepository.saveAll(boards);
    }

    @DisplayName("검색 키워드가 포함된 모집글을 조회할 수 있다.")
    @Test
    void findByRecruitBoardsContaining() {
        //given
        Pageable pageable = getPageable();
        RecruitBoardSearchCondition condition = RecruitBoardSearchCondition.builder()
                .keyword("노인")
                .pageable(pageable)
                .build();

        //when
        Page<RecruitBoardWithCenter> findBoards = recruitBoardRepository.findByRecruitBoardsContaining(condition);

        //then
        assertThat(findBoards).isNotNull();
        assertThat(findBoards.getTotalElements()).isEqualTo(4);
        assertThat(findBoards.getSize()).isEqualTo(5);
        assertThat(findBoards.getTotalPages()).isEqualTo(1);
    }

//    @DisplayName("키워드 없이 검색시 전체 모집글을 조회할 수 있다.")
//    @Test
//    void findByRecruitBoardsContainingWithNull() {
//        //given
//        Pageable pageable = getPageable();
//        RecruitBoardSearchCondition condition = RecruitBoardSearchCondition.builder()
//                .keyword("")
//                .pageable(pageable)
//                .build();
//
//        //when
//        Page<RecruitBoardWithCenter> findBoards = recruitBoardRepository.findByRecruitBoardsContaining(condition);
//
//        //then
//        assertThat(findBoards).isNotNull();
//        assertThat(findBoards.getTotalElements()).isEqualTo(23);
//        assertThat(findBoards.getSize()).isEqualTo(5);
//        assertThat(findBoards.getTotalPages()).isEqualTo(5);
//    }

    private Pageable getPageable() {
        Sort sort = Sort.by(Sort.Order.desc("created_at"));
        return PageRequest.of(0, 5, sort);
    }
}
