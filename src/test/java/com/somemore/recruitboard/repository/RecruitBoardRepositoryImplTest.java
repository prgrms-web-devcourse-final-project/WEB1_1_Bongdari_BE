package com.somemore.recruitboard.repository;

import static com.somemore.common.fixture.CenterFixture.createCenter;
import static com.somemore.common.fixture.LocalDateTimeFixture.createCurrentDateTime;
import static com.somemore.common.fixture.LocationFixture.createLocation;
import static com.somemore.common.fixture.RecruitBoardFixture.createCompletedRecruitBoard;
import static com.somemore.common.fixture.RecruitBoardFixture.createRecruitBoard;
import static com.somemore.recruitboard.domain.RecruitStatus.CLOSED;
import static com.somemore.recruitboard.domain.VolunteerCategory.ADMINISTRATIVE_SUPPORT;
import static org.assertj.core.api.Assertions.assertThat;

import com.somemore.IntegrationTestSupport;
import com.somemore.center.domain.Center;
import com.somemore.center.repository.CenterRepository;
import com.somemore.location.domain.Location;
import com.somemore.location.repository.LocationRepository;
import com.somemore.recruitboard.domain.RecruitBoard;
import com.somemore.recruitboard.domain.RecruitStatus;
import com.somemore.recruitboard.domain.VolunteerCategory;
import com.somemore.recruitboard.dto.condition.RecruitBoardNearByCondition;
import com.somemore.recruitboard.dto.condition.RecruitBoardSearchCondition;
import com.somemore.recruitboard.repository.mapper.RecruitBoardDetail;
import com.somemore.recruitboard.repository.mapper.RecruitBoardWithCenter;
import com.somemore.recruitboard.repository.mapper.RecruitBoardWithLocation;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class RecruitBoardRepositoryImplTest extends IntegrationTestSupport {

    @Autowired
    private RecruitBoardRepositoryImpl recruitBoardRepository;

    @Autowired
    private CenterRepository centerRepository;

    @Autowired
    private LocationRepository locationRepository;

    private final List<RecruitBoard> boards = new ArrayList<>();

    @BeforeEach
    void setUp() {
        Location location = createLocation();
        locationRepository.save(location);

        Center center = createCenter();
        centerRepository.save(center);

        for (int i = 1; i <= 100; i++) {
            String title = "제목" + i;
            RecruitBoard board = createRecruitBoard(title, center.getId(), location.getId());
            boards.add(board);
        }
        recruitBoardRepository.saveAll(boards);
    }

    @DisplayName("논리 삭제된 데이터를 id로 조회시 빈 Optional 반환된다")
    @Test
    void findById() {
        // given
        RecruitBoard deletedBoard = createRecruitBoard();
        deletedBoard.markAsDeleted();
        recruitBoardRepository.save(deletedBoard);

        Long deletedId = deletedBoard.getId();

        // when
        Optional<RecruitBoard> findBoard = recruitBoardRepository.findById(deletedId);

        // then
        assertThat(findBoard).isEmpty();
    }

    @DisplayName("존재하지 않는 아이디로 봉사 모집글과 작성기관을 조회하면 Optional.empty()가 반환된다.")
    @Test
    void findWithCenterByIdWithNotExistId() {
        // given
        Location location = createLocation("특별한주소");
        locationRepository.save(location);

        RecruitBoard deletedRecruitBoard = createRecruitBoard(location.getId());
        deletedRecruitBoard.markAsDeleted();
        recruitBoardRepository.save(deletedRecruitBoard);

        // when
        Optional<RecruitBoardWithLocation> findOne = recruitBoardRepository.findWithLocationById(
                deletedRecruitBoard.getId());

        // then
        assertThat(findOne).isEmpty();
    }

    @DisplayName("조건 없이 모집 게시글을 조회한다. (정렬 포함)")
    @Test
    void findAllWithCenterWithoutCriteria() {
        // given
        Pageable pageable = getPageable();

        RecruitBoardSearchCondition condition = RecruitBoardSearchCondition.builder()
                .pageable(pageable)
                .build();

        // when
        Page<RecruitBoardWithCenter> result = recruitBoardRepository.findAllWithCenter(condition);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(boards.size());
        assertThat(result.getSize()).isEqualTo(5);
        assertThat(result.getNumber()).isZero();
        assertThat(result.getContent()).hasSize(5);

        assertThat(result.getContent().get(0).recruitBoard().getCreatedAt())
                .isAfterOrEqualTo(result.getContent().get(1).recruitBoard().getCreatedAt());
    }

    @DisplayName("키워드로 조회할 수 있다")
    @Test
    void findAllWithCenterByKeyword() {
        // given
        Center center = createCenter();
        centerRepository.save(center);
        String keyword = "키워드";
        RecruitBoard recruitBoard = createRecruitBoard("키워드 조회 제목", center.getId());
        recruitBoardRepository.save(recruitBoard);

        Pageable pageable = getPageable();
        RecruitBoardSearchCondition condition = RecruitBoardSearchCondition.builder()
                .keyword(keyword)
                .pageable(pageable)
                .build();

        // when
        Page<RecruitBoardWithCenter> result = recruitBoardRepository.findAllWithCenter(condition);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getSize()).isEqualTo(5);
        assertThat(result.getNumber()).isZero();
        assertThat(result.getContent()).hasSize(1);

        assertThat(result.getContent().getFirst().recruitBoard().getTitle())
                .isEqualTo("키워드 조회 제목");
    }

    @DisplayName("봉사활동 유형으로 조회할 수 있다")
    @Test
    void findAllWithCenterByCategory() {
        // given
        Center center = createCenter();
        centerRepository.save(center);

        RecruitBoard recruitBoard = createRecruitBoard(ADMINISTRATIVE_SUPPORT, center.getId());
        recruitBoardRepository.save(recruitBoard);

        Pageable pageable = getPageable();
        VolunteerCategory category = ADMINISTRATIVE_SUPPORT;
        RecruitBoardSearchCondition condition = RecruitBoardSearchCondition.builder()
                .category(category)
                .pageable(pageable)
                .build();

        // when
        Page<RecruitBoardWithCenter> result = recruitBoardRepository.findAllWithCenter(condition);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getSize()).isEqualTo(5);
        assertThat(result.getNumber()).isZero();
        assertThat(result.getContent()).hasSize(1);

        assertThat(result.getContent().getFirst().recruitBoard().getRecruitmentInfo()
                .getVolunteerCategory()).isEqualTo(category);
    }

    @DisplayName("지역으로 조회할 수 있다")
    @Test
    void findAllWithCenterByRegion() {
        // given
        Center center = createCenter();
        centerRepository.save(center);

        String region = "특수지역";
        RecruitBoard recruitBoard = createRecruitBoard(center.getId());
        recruitBoard.updateWith(region);
        recruitBoardRepository.save(recruitBoard);

        Pageable pageable = getPageable();

        RecruitBoardSearchCondition condition = RecruitBoardSearchCondition.builder()
                .region(region)
                .pageable(pageable)
                .build();

        // when
        Page<RecruitBoardWithCenter> result = recruitBoardRepository.findAllWithCenter(condition);

    }

    @DisplayName("센터 ID로 완료되지 않은 모집 게시글들의 ID를 조회할 수 있다")
    @Test
    void findNotCompletedIdsByCenterIds() {
        // given
        UUID centerId = UUID.randomUUID();

        RecruitBoard deletedRecruitingBoard = createRecruitBoard(centerId);
        deletedRecruitingBoard.markAsDeleted();
        recruitBoardRepository.save(deletedRecruitingBoard);

        RecruitBoard recruitingBoard = createRecruitBoard(centerId);
        recruitBoardRepository.save(recruitingBoard);

        RecruitBoard deletedClosedBoard = createRecruitBoard(centerId);
        deletedClosedBoard.changeRecruitStatus(RecruitStatus.CLOSED, createCurrentDateTime());
        deletedClosedBoard.markAsDeleted();
        recruitBoardRepository.save(deletedClosedBoard);

        RecruitBoard closedBoard = createRecruitBoard(centerId);
        closedBoard.changeRecruitStatus(RecruitStatus.CLOSED, createCurrentDateTime());
        recruitBoardRepository.save(closedBoard);

        RecruitBoard deletedCompletedRecruitBoard = createCompletedRecruitBoard();
        deletedCompletedRecruitBoard.markAsDeleted();
        recruitBoardRepository.save(deletedCompletedRecruitBoard);

        RecruitBoard completedRecruitBoard = createCompletedRecruitBoard();
        recruitBoardRepository.save(completedRecruitBoard);

        // when
        List<Long> notCompletedBoardIds = recruitBoardRepository.findNotCompletedIdsByCenterId(
                centerId);

        // then
        assertThat(notCompletedBoardIds)
                .hasSize(2)
                .doesNotContain(deletedRecruitingBoard.getId())
                .doesNotContain(deletedClosedBoard.getId())
                .doesNotContain(deletedCompletedRecruitBoard.getId())
                .doesNotContain(completedRecruitBoard.getId());
    }

    @DisplayName("시간 인증 여부로 조회할 수 있다")
    @Test
    void findAllWithCenterByAdmitted() {
        // given
        Center center = createCenter();
        centerRepository.save(center);

        Boolean admitted = false;
        RecruitBoard recruitBoard = createRecruitBoard(admitted, center.getId());
        recruitBoardRepository.save(recruitBoard);

        Pageable pageable = getPageable();

        RecruitBoardSearchCondition condition = RecruitBoardSearchCondition.builder()
                .admitted(false)
                .pageable(pageable)
                .build();

        // when
        Page<RecruitBoardWithCenter> result = recruitBoardRepository.findAllWithCenter(condition);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getSize()).isEqualTo(5);
        assertThat(result.getNumber()).isZero();
        assertThat(result.getContent()).hasSize(1);

        assertThat(result.getContent().getFirst().recruitBoard().getRecruitmentInfo()
                .getAdmitted()).isFalse();
    }

    @DisplayName("모집글 상태로 조회할 수 있다.")
    @Test
    void findAllWithCenterByStatus() {
        // given
        Center center = createCenter();
        centerRepository.save(center);

        RecruitStatus status = CLOSED;
        RecruitBoard recruitBoard = createRecruitBoard(center.getId());
        LocalDateTime currentDateTime = createCurrentDateTime();
        recruitBoard.changeRecruitStatus(status, currentDateTime);
        recruitBoardRepository.save(recruitBoard);

        Pageable pageable = getPageable();

        RecruitBoardSearchCondition condition = RecruitBoardSearchCondition.builder()
                .status(status)
                .pageable(pageable)
                .build();

        // when
        Page<RecruitBoardWithCenter> result = recruitBoardRepository.findAllWithCenter(condition);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getSize()).isEqualTo(5);
        assertThat(result.getNumber()).isZero();
        assertThat(result.getContent()).hasSize(1);

        assertThat(result.getContent().getFirst().recruitBoard().getRecruitStatus()).isEqualTo(
                status);
    }

    @DisplayName("위치 기반으로 반경 내에 모집글을 반환한다")
    @Test
    void findAllNearByLocation() {
        // given
        Pageable pageable = getPageable();

        RecruitBoardNearByCondition condition = RecruitBoardNearByCondition.builder()
                .latitude(37.5935)
                .longitude(126.9780)
                .radius(5.0)
                .pageable(pageable)
                .build();

        // when
        Page<RecruitBoardDetail> result = recruitBoardRepository.findAllNearby(condition);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(boards.size());
        assertThat(result.getContent()).isNotEmpty();
    }

    @DisplayName("위치 기반으로 반경 내에 모집글이 없으면 빈 페이지를 반환한다")
    @Test
    void findAllNearByLocation_noResult() {
        // given
        Pageable pageable = getPageable();

        RecruitBoardNearByCondition condition = RecruitBoardNearByCondition.builder()
                .latitude(37.6115)
                .longitude(127.034)
                .radius(5.0)
                .pageable(pageable)
                .build();

        // when
        Page<RecruitBoardDetail> result = recruitBoardRepository.findAllNearby(condition);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isZero();
        assertThat(result.getContent()).isEmpty();

    }

    @DisplayName("기관 아이디로 모집글 리스트를 조회할 수 있다.")
    @Test
    void findAllByCenterId() {
        // given
        Center center = createCenter();
        centerRepository.save(center);

        RecruitBoard recruitBoard = createRecruitBoard(center.getId());
        recruitBoardRepository.save(recruitBoard);

        Pageable pageable = getPageable();
        RecruitBoardSearchCondition condition = RecruitBoardSearchCondition.builder()
                .pageable(pageable)
                .build();

        // when
        Page<RecruitBoard> result = recruitBoardRepository.findAllByCenterId(center.getId(),
                condition);

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).hasSize(1);
    }

    @DisplayName("잘못된 기관 아이디로 모집글 리스트를 조회하면 빈 리스트가 반환된다.")
    @Test
    void findAllByCenterIdWhenWrongCenterId() {
        // given
        UUID centerId = UUID.randomUUID();
        Pageable pageable = getPageable();
        RecruitBoardSearchCondition condition = RecruitBoardSearchCondition.builder()
                .pageable(pageable)
                .build();

        // when
        Page<RecruitBoard> results = recruitBoardRepository.findAllByCenterId(centerId,
                condition);

        // then
        assertThat(results).isEmpty();
    }

    @DisplayName("아이디 리스트로 모집글을 조회할 수 있다.")
    @Test
    void findAllByIds() {
        // given
        RecruitBoard board1 = createRecruitBoard();
        RecruitBoard board2 = createRecruitBoard();
        RecruitBoard board3 = createRecruitBoard();

        recruitBoardRepository.saveAll(List.of(board1, board2, board3));
        List<Long> ids = List.of(board1.getId(), board2.getId(), board3.getId(), 100000L);

        // when
        List<RecruitBoard> all = recruitBoardRepository.findAllByIds(ids);

        // then
        assertThat(all).hasSize(3);
    }

    private Pageable getPageable() {
        Sort sort = Sort.by(Sort.Order.desc("created_at"));
        return PageRequest.of(0, 5, sort);
    }

}
