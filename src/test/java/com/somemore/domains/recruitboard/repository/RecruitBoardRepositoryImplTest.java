package com.somemore.domains.recruitboard.repository;

import static com.somemore.center.domain.NEWCenter.createDefault;
import static com.somemore.domains.recruitboard.domain.RecruitStatus.CLOSED;
import static com.somemore.domains.recruitboard.domain.RecruitStatus.COMPLETED;
import static com.somemore.domains.recruitboard.domain.RecruitStatus.RECRUITING;
import static com.somemore.domains.recruitboard.domain.VolunteerCategory.ADMINISTRATIVE_SUPPORT;
import static com.somemore.domains.recruitboard.domain.VolunteerCategory.OTHER;
import static com.somemore.support.fixture.LocationFixture.createLocation;
import static org.assertj.core.api.Assertions.assertThat;

import com.somemore.center.domain.NEWCenter;
import com.somemore.center.repository.NEWCenterRepository;
import com.somemore.domains.location.domain.Location;
import com.somemore.domains.location.repository.LocationRepository;
import com.somemore.domains.recruitboard.domain.RecruitBoard;
import com.somemore.domains.recruitboard.domain.RecruitStatus;
import com.somemore.domains.recruitboard.domain.RecruitmentInfo;
import com.somemore.domains.recruitboard.domain.VolunteerCategory;
import com.somemore.domains.recruitboard.dto.condition.RecruitBoardNearByCondition;
import com.somemore.domains.recruitboard.dto.condition.RecruitBoardSearchCondition;
import com.somemore.domains.recruitboard.repository.mapper.RecruitBoardDetail;
import com.somemore.domains.recruitboard.repository.mapper.RecruitBoardWithCenter;
import com.somemore.domains.recruitboard.repository.mapper.RecruitBoardWithLocation;
import com.somemore.support.IntegrationTestSupport;
import com.somemore.user.domain.UserCommonAttribute;
import com.somemore.user.domain.UserRole;
import com.somemore.user.repository.usercommonattribute.UserCommonAttributeRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
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
    private NEWCenterRepository centerRepository;

    @Autowired
    private UserCommonAttributeRepository userCommonAttributeRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private EntityManager em;

    private Location location;
    private UserCommonAttribute userCommonAttribute;
    private NEWCenter center;
    private RecruitBoard board;

    @BeforeEach
    void setUp() {
        location = createLocation();
        locationRepository.save(location);

        UUID userId = UUID.randomUUID();

        userCommonAttribute = createUserCommonAttribute(userId);
        userCommonAttributeRepository.save(userCommonAttribute);

        center = createCenter(userId);
        centerRepository.save(center);

        board = createRecruitBoard(center.getId(), location.getId(), RECRUITING);
        recruitBoardRepository.save(board);
    }

    @DisplayName("아이디로 모집글을 조회할 수 있다.")
    @Test
    void findById() {
        // given
        Long id = board.getId();

        // when
        Optional<RecruitBoard> findBoard = recruitBoardRepository.findById(id);

        // then
        assertThat(findBoard).isNotEmpty();
        assertThat(findBoard.get().getId()).isEqualTo(id);
    }

    @DisplayName("아이디로 봉사 모집글과 작성기관을 조회할 수 있다.")
    @Test
    void findWithLocationById() {
        // given
        Long id = board.getId();

        // when
        Optional<RecruitBoardWithLocation> findOne = recruitBoardRepository.findWithLocationById(
                id);

        // then
        assertThat(findOne).isNotEmpty();
        RecruitBoardWithLocation boardWithLocation = findOne.get();
        assertThat(boardWithLocation.recruitBoard().getId()).isEqualTo(id);
        assertThat(boardWithLocation.address()).isEqualTo(location.getAddress());
        assertThat(boardWithLocation.latitude()).isEqualByComparingTo(location.getLatitude());
        assertThat(boardWithLocation.longitude()).isEqualByComparingTo(location.getLongitude());
    }

    @DisplayName("조건 없이 모집 게시글을 조회한다.")
    @Test
    void findAllWithCenterWithoutCondition() {
        // given
        Pageable pageable = getPageable();

        RecruitBoardSearchCondition condition = RecruitBoardSearchCondition.builder()
                .pageable(pageable)
                .build();

        // when
        Page<RecruitBoardWithCenter> result = recruitBoardRepository.findAllWithCenter(condition);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @DisplayName("키워드로 조회할 수 있다")
    @Test
    void findAllWithCenterByKeyword() {
        // given
        String keyword = "키워드";
        String title = keyword + " 제목";
        RecruitBoard recruitBoard = createRecruitBoard(center.getId(), title, OTHER, "지역", false,
                RECRUITING);
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
        assertThat(result.getContent()).hasSize(1);

        assertThat(result.getContent().getFirst().recruitBoard().getTitle())
                .isEqualTo(title);
    }

    @DisplayName("봉사활동 유형으로 조회할 수 있다")
    @Test
    void findAllWithCenterByCategory() {
        // given
        VolunteerCategory category = ADMINISTRATIVE_SUPPORT;
        RecruitBoard recruitBoard = createRecruitBoard(center.getId(), "제목", category, "지역", false,
                RECRUITING);
        recruitBoardRepository.save(recruitBoard);

        Pageable pageable = getPageable();
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

        assertThat(result.getContent().getFirst().recruitBoard().getRecruitmentInfo()
                .getVolunteerCategory()).isEqualTo(category);
    }

    @DisplayName("지역으로 조회할 수 있다")
    @Test
    void findAllWithCenterByRegion() {
        // given
        String region = "특수지역";
        RecruitBoard recruitBoard = createRecruitBoard(center.getId(), "제목", OTHER, region, false,
                RECRUITING);
        recruitBoardRepository.save(recruitBoard);

        Pageable pageable = getPageable();

        RecruitBoardSearchCondition condition = RecruitBoardSearchCondition.builder()
                .region(region)
                .pageable(pageable)
                .build();

        // when
        Page<RecruitBoardWithCenter> result = recruitBoardRepository.findAllWithCenter(condition);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.getContent().getFirst().recruitBoard().getId()).isEqualTo(
                recruitBoard.getId());
    }

    @DisplayName("시간 인증 여부로 조회할 수 있다")
    @Test
    void findAllWithCenterByAdmitted() {
        // given
        boolean admitted = false;
        RecruitBoard recruitBoard = createRecruitBoard(center.getId(), "제목", OTHER, "지역", admitted,
                RECRUITING);
        recruitBoardRepository.save(recruitBoard);

        Pageable pageable = getPageable();

        RecruitBoardSearchCondition condition = RecruitBoardSearchCondition.builder()
                .admitted(admitted)
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
        RecruitStatus status = CLOSED;
        RecruitBoard recruitBoard = createRecruitBoard(center.getId(), "제목", OTHER, "지역", true,
                status);
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
        assertThat(result.getTotalElements()).isEqualTo(1);
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
        UUID centerId = UUID.randomUUID();
        RecruitBoard recruitBoard = createRecruitBoard(centerId, 1L, RECRUITING);
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
        UUID wrongId = UUID.randomUUID();
        Pageable pageable = getPageable();
        RecruitBoardSearchCondition condition = RecruitBoardSearchCondition.builder()
                .pageable(pageable)
                .build();

        // when
        Page<RecruitBoard> results = recruitBoardRepository.findAllByCenterId(wrongId, condition);

        // then
        assertThat(results).isEmpty();
    }

    @DisplayName("센터 ID로 완료되지 않은 모집 게시글들의 ID를 조회할 수 있다")
    @Test
    void findNotCompletedIdsByCenterIds() {
        // given
        UUID centerId = UUID.randomUUID();

        RecruitBoard recruitingBoard = createRecruitBoard(centerId, 1L, RECRUITING);
        recruitBoardRepository.save(recruitingBoard);

        RecruitBoard closedBoard = createRecruitBoard(centerId, 1L, CLOSED);
        recruitBoardRepository.save(closedBoard);

        RecruitBoard completedRecruitBoard = createRecruitBoard(centerId, 1L, COMPLETED);
        recruitBoardRepository.save(completedRecruitBoard);

        // when
        List<Long> notCompletedBoardIds = recruitBoardRepository.findNotCompletedIdsByCenterId(
                centerId);

        // then
        assertThat(notCompletedBoardIds)
                .hasSize(2)
                .doesNotContain(completedRecruitBoard.getId());
    }

    @DisplayName("아이디 리스트로 모집글을 조회할 수 있다.")
    @Test
    void findAllByIds() {
        // given
        RecruitBoard board1 = createRecruitBoard(UUID.randomUUID(), 1L, RECRUITING);
        RecruitBoard board2 = createRecruitBoard(UUID.randomUUID(), 1L, RECRUITING);
        RecruitBoard board3 = createRecruitBoard(UUID.randomUUID(), 1L, RECRUITING);

        recruitBoardRepository.saveAll(List.of(board1, board2, board3));
        List<Long> ids = List.of(board1.getId(), board2.getId(), board3.getId(), 100000L);

        // when
        List<RecruitBoard> all = recruitBoardRepository.findAllByIds(ids);

        // then
        assertThat(all).hasSize(3);
    }

    @DisplayName("봉사 시작일 기준으로 모집중 상태 게시글을 모집 완료로 변경한다")
    @Test
    void updateStatusToClosedForDateRange() {
        // given
        LocalDateTime today = LocalDateTime.of(2024, 1, 1, 0, 0); // 2024-01-01 00:00:00
        LocalDateTime startDateTime = today.plusHours(12); // 2024-01-01 12:00:00
        LocalDateTime endDateTime = startDateTime.plusHours(2); // 2024-01-01 14:00:00
        LocalDateTime tomorrow = today.plusDays(1); // 2024-01-02 00:00:00

        RecruitBoard boardOne = createRecruitBoard(startDateTime, endDateTime, RECRUITING);
        RecruitBoard boardTwo = createRecruitBoard(startDateTime, endDateTime, RECRUITING);
        recruitBoardRepository.saveAll(List.of(boardOne, boardTwo));

        // when
        long updateCnt = recruitBoardRepository.updateStatusToClosedForDateRange(today, tomorrow);
        em.clear();

        // then
        assertThat(updateCnt).isEqualTo(2);
        RecruitBoard one = recruitBoardRepository.findById(boardOne.getId()).orElseThrow();
        RecruitBoard two = recruitBoardRepository.findById(boardTwo.getId()).orElseThrow();
        assertThat(one.getRecruitStatus()).isEqualTo(CLOSED);
        assertThat(two.getRecruitStatus()).isEqualTo(CLOSED);
    }

    @DisplayName("봉사 종료일 기준으로 모집완료 상태 게시글을 종료로 변경한다")
    @Test
    void updateStatusToCompletedForDateRange() {
        // given
        LocalDateTime now = LocalDateTime.of(2024, 1, 2, 0, 0); // 2024-01-02 00:00:00
        LocalDateTime yesterday = now.minusDays(1); // 2024-01-01 00:00:00
        LocalDateTime startDateTime = yesterday.plusHours(12); // 2024-01-01 12:00:00
        LocalDateTime endDateTime = startDateTime.plusHours(2); // 2024-01-01 14:00:00

        RecruitBoard boardOne = createRecruitBoard(startDateTime, endDateTime, CLOSED);
        RecruitBoard boardTwo = createRecruitBoard(startDateTime, endDateTime, CLOSED);
        recruitBoardRepository.saveAll(List.of(boardOne, boardTwo));

        // when
        long updateCnt = recruitBoardRepository.updateStatusToCompletedForDateRange(yesterday, now);
        em.clear();

        // then
        assertThat(updateCnt).isEqualTo(2);

        RecruitBoard one = recruitBoardRepository.findById(boardOne.getId()).orElseThrow();
        RecruitBoard two = recruitBoardRepository.findById(boardTwo.getId()).orElseThrow();
        assertThat(one.getRecruitStatus()).isEqualTo(COMPLETED);
        assertThat(two.getRecruitStatus()).isEqualTo(COMPLETED);
    }

//    @DisplayName("모집글을 elastic search index에 저장할 수 있다. (repository)")
//    @Test
//    void saveDocuments() {
//        //given
//        Pageable pageable = getPageable();
//        RecruitBoardSearchCondition condition = RecruitBoardSearchCondition.builder()
//                .keyword("저장")
//                .pageable(pageable)
//                .build();
//
//        List<RecruitBoard> recruitBoards = new ArrayList<>();
//
//        RecruitBoard board1 = createRecruitBoard("저장 잘 되나요?", centerId);
//        RecruitBoard savedBoard1 = recruitBoardRepository.save(board1);
//        RecruitBoard board2 = createRecruitBoard("저장해줘", centerId);
//        RecruitBoard savedBoard2 = recruitBoardRepository.save(board2);
//        recruitBoards.add(savedBoard1);
//        recruitBoards.add(savedBoard2);
//
//        //when
//        recruitBoardRepository.saveDocuments(recruitBoards);
//
//        //then
//        Page<RecruitBoardWithCenter> findBoard = recruitBoardRepository.findByRecruitBoardsContaining(condition);
//
//        assertThat(findBoard).isNotNull();
//        assertThat(findBoard.getTotalElements()).isEqualTo(2);
//
//        recruitBoardRepository.deleteDocument(savedBoard1.getId());
//        recruitBoardRepository.deleteDocument(savedBoard2.getId());
//    }

    private static UserCommonAttribute createUserCommonAttribute(UUID userId) {
        return UserCommonAttribute.createDefault(userId, UserRole.CENTER);
    }

    private static NEWCenter createCenter(UUID userId) {
        return createDefault(userId);
    }

    private static RecruitBoard createRecruitBoard(UUID centerId, Long locationId,
            RecruitStatus status) {

        RecruitmentInfo recruitmentInfo = RecruitmentInfo.builder()
                .region("지역")
                .recruitmentCount(1)
                .volunteerStartDateTime(LocalDateTime.now())
                .volunteerEndDateTime(LocalDateTime.now())
                .volunteerHours(10)
                .volunteerCategory(OTHER)
                .admitted(true)
                .build();

        return RecruitBoard.builder()
                .centerId(centerId)
                .locationId(locationId)
                .title("모집글 제목")
                .content("모집글 내용")
                .recruitmentInfo(recruitmentInfo)
                .status(status)
                .build();
    }

    private static RecruitBoard createRecruitBoard(UUID centerId, String title,
            VolunteerCategory category,
            String region, boolean admitted, RecruitStatus status) {

        RecruitmentInfo recruitmentInfo = RecruitmentInfo.builder()
                .region(region)
                .recruitmentCount(1)
                .volunteerStartDateTime(LocalDateTime.now())
                .volunteerEndDateTime(LocalDateTime.now())
                .volunteerHours(10)
                .volunteerCategory(category)
                .admitted(admitted)
                .build();

        return RecruitBoard.builder()
                .centerId(centerId)
                .locationId(1L)
                .title(title)
                .content("모집글 내용")
                .recruitmentInfo(recruitmentInfo)
                .status(status)
                .build();
    }

    private static RecruitBoard createRecruitBoard(LocalDateTime startTime, LocalDateTime endTime,
            RecruitStatus status) {

        RecruitmentInfo recruitmentInfo = RecruitmentInfo.builder()
                .region("지역")
                .recruitmentCount(1)
                .volunteerStartDateTime(startTime)
                .volunteerEndDateTime(endTime)
                .volunteerHours(10)
                .volunteerCategory(OTHER)
                .admitted(true)
                .build();

        return RecruitBoard.builder()
                .centerId(UUID.randomUUID())
                .locationId(1L)
                .title("모집글 제목")
                .content("모집글 내용")
                .recruitmentInfo(recruitmentInfo)
                .status(status)
                .build();
    }

    private Pageable getPageable() {
        Sort sort = Sort.by(Sort.Order.desc("created_at"));
        return PageRequest.of(0, 5, sort);
    }

}
