package com.somemore.domains.recruitboard.service;

import static com.somemore.center.domain.NEWCenter.createDefault;
import static com.somemore.domains.recruitboard.domain.RecruitStatus.CLOSED;
import static com.somemore.domains.recruitboard.domain.RecruitStatus.COMPLETED;
import static com.somemore.domains.recruitboard.domain.RecruitStatus.RECRUITING;
import static com.somemore.domains.recruitboard.domain.VolunteerCategory.OTHER;
import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_RECRUIT_BOARD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.somemore.center.domain.NEWCenter;
import com.somemore.center.repository.NEWCenterRepository;
import com.somemore.domains.location.domain.Location;
import com.somemore.domains.location.repository.LocationRepository;
import com.somemore.domains.recruitboard.domain.RecruitBoard;
import com.somemore.domains.recruitboard.domain.RecruitStatus;
import com.somemore.domains.recruitboard.domain.RecruitmentInfo;
import com.somemore.domains.recruitboard.dto.condition.RecruitBoardNearByCondition;
import com.somemore.domains.recruitboard.dto.condition.RecruitBoardSearchCondition;
import com.somemore.domains.recruitboard.dto.response.RecruitBoardDetailResponseDto;
import com.somemore.domains.recruitboard.dto.response.RecruitBoardResponseDto;
import com.somemore.domains.recruitboard.dto.response.RecruitBoardWithCenterResponseDto;
import com.somemore.domains.recruitboard.dto.response.RecruitBoardWithLocationResponseDto;
import com.somemore.domains.recruitboard.repository.RecruitBoardRepository;
import com.somemore.global.exception.BadRequestException;
import com.somemore.support.IntegrationTestSupport;
import com.somemore.user.domain.UserCommonAttribute;
import com.somemore.user.domain.UserRole;
import com.somemore.user.repository.usercommonattribute.UserCommonAttributeRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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
class RecruitBoardQueryServiceTest extends IntegrationTestSupport {

    @Autowired
    private RecruitBoardQueryService recruitBoardQueryService;

    @Autowired
    private RecruitBoardRepository recruitBoardRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private NEWCenterRepository centerRepository;

    @Autowired
    private UserCommonAttributeRepository userCommonAttributeRepository;

    private UserCommonAttribute userCommonAttribute;
    private NEWCenter center;
    private RecruitBoard recruitBoard;
    private Location location;

    @BeforeEach
    void setUp() {
        UUID userId = UUID.randomUUID();

        userCommonAttribute = createUserCommonAttribute(userId);
        userCommonAttributeRepository.save(userCommonAttribute);

        center = createCenter(userId);
        centerRepository.save(center);

        location = createLocation();
        locationRepository.save(location);

        recruitBoard = createRecruitBoard(center.getId(), location.getId());
        recruitBoardRepository.save(recruitBoard);
    }

    @DisplayName("존재하는 ID가 주어지면 RecruitBoard 엔티티를 조회할 수 있다")
    @Test
    void getByIdWithExistsId() {
        // given
        Long id = recruitBoard.getId();

        // when
        RecruitBoardResponseDto dto = recruitBoardQueryService.getRecruitBoardById(id);

        // then
        assertThat(dto.id()).isEqualTo(recruitBoard.getId());
    }

    @DisplayName("존재하지 않는 ID가 주어지면 에러가 발생한다.")
    @Test
    void getByIdWithDoesNotExistId() {
        // given
        Long wrongId = 999L;

        // when
        // then
        assertThatThrownBy(
                () -> recruitBoardQueryService.getRecruitBoardById(wrongId))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(NOT_EXISTS_RECRUIT_BOARD.getMessage());
    }

    @DisplayName("아이디로 모집글과 기관를 조회할 수 있다.")
    @Test
    void getWithCenterById() {
        // given
        Long id = recruitBoard.getId();

        // when
        RecruitBoardWithLocationResponseDto responseDto = recruitBoardQueryService.getWithLocationById(
                id);

        // then
        assertThat(responseDto.id()).isEqualTo(id);
        assertThat(responseDto.location().address()).isEqualTo(location.getAddress());
    }

    @DisplayName("존재하지 않는 아이디로 모집글과 센터를 조회하면 에러가 발생한다.")
    @Test
    void getWithCenterByIdWhenNotExistId() {
        // given
        Long wrongId = 9999L;

        // when
        // then
        assertThatThrownBy(
                () -> recruitBoardQueryService.getWithLocationById(wrongId))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(NOT_EXISTS_RECRUIT_BOARD.getMessage());
    }

    @DisplayName("모집글과 기관 정보 리스트를 페이징 처리하여 받을 수 있다")
    @Test
    void getAllWithCenter() {
        // given
        Pageable pageable = getPageable();
        RecruitBoardSearchCondition condition = RecruitBoardSearchCondition.builder()
                .pageable(pageable)
                .build();

        // when
        Page<RecruitBoardWithCenterResponseDto> dtos = recruitBoardQueryService.getAllWithCenter(
                condition);

        // then
        assertThat(dtos).isNotEmpty();
        assertThat(dtos.getTotalElements()).isEqualTo(1);
        assertThat(dtos.getContent().getFirst().center().name()).isEqualTo(
                userCommonAttribute.getName());
    }

    @DisplayName("위치 기반으로 주변 모집글을 페이징하여 조회할 수 있다")
    @Test
    void getRecruitBoardsNearBy() {
        // given
        Pageable pageable = getPageable();
        RecruitBoardNearByCondition condition = RecruitBoardNearByCondition.builder()
                .latitude(location.getLatitude().doubleValue())
                .longitude(location.getLongitude().doubleValue())
                .radius(3.0)
                .pageable(pageable)
                .build();

        // when
        Page<RecruitBoardDetailResponseDto> result = recruitBoardQueryService.getRecruitBoardsNearby(
                condition);

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().getFirst().id()).isEqualTo(recruitBoard.getId());
    }

    @DisplayName("기관 아이디로 모집글을 페이징하여 조회할 수 있다")
    @Test
    void getRecruitBoardsByCenterId() {
        // given
        RecruitBoard one = createRecruitBoard(center.getId(), location.getId());
        RecruitBoard two = createRecruitBoard(center.getId(), location.getId());
        RecruitBoard three = createRecruitBoard(center.getId(), location.getId());
        recruitBoardRepository.saveAll(List.of(one, two, three));

        UUID centerId = center.getId();
        Pageable pageable = getPageable();
        RecruitBoardSearchCondition condition = RecruitBoardSearchCondition.builder()
                .pageable(pageable)
                .build();

        // when
        Page<RecruitBoardResponseDto> result = recruitBoardQueryService.getRecruitBoardsByCenterId(
                centerId, condition);

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.getTotalElements()).isEqualTo(4);
    }

    @DisplayName("센터 ID로 완료되지 않은 모집 게시글들의 ID를 조회할 수 있다")
    @Test
    void findNotCompletedIdsByCenterIds() {
        // given
        UUID centerId = UUID.randomUUID();

        RecruitBoard recruitingBoard = createRecruitBoard(centerId, RECRUITING);
        recruitBoardRepository.save(recruitingBoard);

        RecruitBoard closedBoard = createRecruitBoard(centerId, CLOSED);
        recruitBoardRepository.save(closedBoard);

        RecruitBoard completedRecruitBoard = createRecruitBoard(centerId, COMPLETED);
        recruitBoardRepository.save(completedRecruitBoard);

        // when
        List<Long> notCompletedBoardIds = recruitBoardQueryService.getNotCompletedIdsByCenterIds(
                centerId);

        // then
        assertThat(notCompletedBoardIds)
                .hasSize(2)
                .doesNotContain(
                        completedRecruitBoard.getId());
    }

    @DisplayName("아이디리스트로 모집글 리스트를 조회할 수 있다.")
    @Test
    void getAllByIds() {
        // given
        RecruitBoard board1 = createRecruitBoard(UUID.randomUUID());
        RecruitBoard board2 = createRecruitBoard(UUID.randomUUID());
        RecruitBoard board3 = createRecruitBoard(UUID.randomUUID());

        recruitBoardRepository.saveAll(List.of(board1, board2, board3));
        List<Long> ids = List.of(board1.getId(), board2.getId(), board3.getId(), 100000L);

        // when
        List<RecruitBoard> all = recruitBoardQueryService.getAllByIds(ids);

        // then
        assertThat(all).hasSize(3);
    }

    private static UserCommonAttribute createUserCommonAttribute(UUID userId) {
        return UserCommonAttribute.createDefault(userId, UserRole.CENTER);
    }

    private static NEWCenter createCenter(UUID userId) {
        return createDefault(userId);
    }

    private static RecruitBoard createRecruitBoard(UUID centerId, Long locationId) {

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
                .status(RECRUITING)
                .build();
    }

    private static RecruitBoard createRecruitBoard(UUID centerId) {

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
                .locationId(200L)
                .title("모집글 제목")
                .content("모집글 내용")
                .recruitmentInfo(recruitmentInfo)
                .status(RECRUITING)
                .build();
    }

    private static RecruitBoard createRecruitBoard(UUID centerId, RecruitStatus status) {

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
                .locationId(200L)
                .title("모집글 제목")
                .content("모집글 내용")
                .recruitmentInfo(recruitmentInfo)
                .status(status)
                .build();
    }

    private static Location createLocation() {
        return Location.builder()
                .address("주소")
                .latitude(BigDecimal.valueOf(37.5665))
                .longitude(BigDecimal.valueOf(126.9780))
                .build();
    }

    private Pageable getPageable() {
        Sort sort = Sort.by(Sort.Order.desc("created_at"));
        return PageRequest.of(0, 5, sort);
    }
}
