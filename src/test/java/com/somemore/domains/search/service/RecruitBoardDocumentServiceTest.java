package com.somemore.domains.search.service;

import com.somemore.domains.center.domain.Center;
import com.somemore.domains.center.repository.center.CenterRepository;
import com.somemore.domains.location.domain.Location;
import com.somemore.domains.location.repository.LocationRepository;
import com.somemore.domains.recruitboard.domain.RecruitBoard;
import com.somemore.domains.recruitboard.dto.condition.RecruitBoardNearByCondition;
import com.somemore.domains.recruitboard.dto.condition.RecruitBoardSearchCondition;
import com.somemore.domains.recruitboard.dto.response.RecruitBoardDetailResponseDto;
import com.somemore.domains.recruitboard.dto.response.RecruitBoardWithCenterResponseDto;
import com.somemore.domains.recruitboard.repository.RecruitBoardRepository;
import com.somemore.domains.search.repository.SearchBoardRepository;
import com.somemore.support.IntegrationTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.somemore.support.fixture.CenterFixture.createCenter;
import static com.somemore.support.fixture.LocationFixture.createLocation;
import static com.somemore.support.fixture.RecruitBoardFixture.createRecruitBoard;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@EnabledIf(value = "${elastic.search.enabled}", loadContext = true)
class RecruitBoardDocumentServiceTest extends IntegrationTestSupport {

    @Autowired
    private RecruitBoardDocumentService recruitBoardDocumentService;

    @Autowired
    private SearchBoardRepository searchBoardRepository;

    @Autowired
    private RecruitBoardRepository recruitBoardRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private CenterRepository centerRepository;

    private final List<RecruitBoard> boards = new ArrayList<>();

    @BeforeEach
    public void setUp() {
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

    @DisplayName("검색 키워드가 포함된 모집글을 조회한다. (elasticsearch)")
    @Test
    void getRecruitBoardBySearch() {
        //given
        Pageable pageable = getPageable();
        RecruitBoardSearchCondition condition = RecruitBoardSearchCondition.builder()
                .keyword("노인")
                .pageable(pageable)
                .build();

        //when
        Page<RecruitBoardWithCenterResponseDto> dtos = recruitBoardDocumentService.getRecruitBoardBySearch(condition);

        //then
        assertThat(dtos).isNotNull();
        assertThat(dtos.getContent()).isNotNull();
        assertThat(dtos.getTotalElements()).isEqualTo(4);
        assertThat(dtos.getSize()).isEqualTo(5);
        assertThat(dtos.getTotalPages()).isEqualTo(1);
    }

    @DisplayName("키워드 없이 검색시 전체 모집글을 조회한다. (elasticsearch)")
    @Test
    void getRecruitBoardBySearchWithNull() {
        //given
        Pageable pageable = getPageable();
        RecruitBoardSearchCondition condition = RecruitBoardSearchCondition.builder()
                .keyword("")
                .pageable(pageable)
                .build();

        //when
        Page<RecruitBoardWithCenterResponseDto> dtos = recruitBoardDocumentService.getRecruitBoardBySearch(condition);

        //then
        assertThat(dtos).isNotNull();
        assertThat(dtos.getContent()).isNotNull();
        assertThat(dtos.getTotalElements()).isEqualTo(25);
        assertThat(dtos.getSize()).isEqualTo(5);
        assertThat(dtos.getTotalPages()).isEqualTo(5);
    }

    @DisplayName("위치 기반으로 반경 내에 검색 키워드가 포함된 모집글을 반환한다. (elasticsearch)")
    @Test
    void findAllNearByLocationWithKeyword() {
        // given
        Pageable pageable = getPageable();

        RecruitBoardNearByCondition condition = RecruitBoardNearByCondition.builder()
                .keyword("도서관")
                .latitude(37.5935)
                .longitude(126.9780)
                .radius(5.0)
                .pageable(pageable)
                .build();

        // when
        Page<RecruitBoardDetailResponseDto> result = recruitBoardDocumentService.getRecruitBoardsNearbyWithKeyword(
                condition);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @DisplayName("키워드 없이 검색 시 위치 기반으로 주변 모집글을 전부 페이징하여 조회할 수 있다. (elasticsearch)")
    @Test
    void getRecruitBoardsNearBy() {
        // given
        Pageable pageable = getPageable();
        RecruitBoardNearByCondition condition = RecruitBoardNearByCondition.builder()
                .keyword(null)
                .latitude(37.5935)
                .longitude(126.9780)
                .radius(5.0)
                .pageable(pageable)
                .build();

        // when
        Page<RecruitBoardDetailResponseDto> result = recruitBoardDocumentService.getRecruitBoardsNearbyWithKeyword(
                condition);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(25);
        assertThat(result.getContent()).isNotEmpty();
    }

    @DisplayName("모집글을 elastic search index에 저장한다. (service)")
    @Test
    void saveRecruitBoardDocuments() {
        //given
        Center center = createCenter("특별한 기관");
        centerRepository.save(center);

        Pageable pageable = getPageable();
        RecruitBoardSearchCondition condition = RecruitBoardSearchCondition.builder()
                .keyword("저장")
                .pageable(pageable)
                .build();

        List<RecruitBoard> recruitBoards = new ArrayList<>();

        RecruitBoard board1 = createRecruitBoard("저장 잘 되나요?", center.getId());
        RecruitBoard savedBoard1 = recruitBoardRepository.save(board1);
        RecruitBoard board2 = createRecruitBoard("저장해줘", center.getId());
        RecruitBoard savedBoard2 = recruitBoardRepository.save(board2);
        recruitBoards.add(savedBoard1);
        recruitBoards.add(savedBoard2);

        //when
        recruitBoardDocumentService.saveRecruitBoardDocuments(recruitBoards);

        //then
        Page<RecruitBoardWithCenterResponseDto> findBoard = recruitBoardDocumentService.getRecruitBoardBySearch(condition);

        assertThat(findBoard).isNotNull();
        assertThat(findBoard.getTotalElements()).isEqualTo(2);

        searchBoardRepository.deleteRecruitBoardDocument(savedBoard1.getId());
        searchBoardRepository.deleteRecruitBoardDocument(savedBoard2.getId());
    }

    private Pageable getPageable() {
        Sort sort = Sort.by(Sort.Order.desc("created_at"));
        return PageRequest.of(0, 5, sort);
    }
}
