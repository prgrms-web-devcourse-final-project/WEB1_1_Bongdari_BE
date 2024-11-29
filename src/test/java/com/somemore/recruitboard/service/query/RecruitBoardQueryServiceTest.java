package com.somemore.recruitboard.service.query;

import static com.somemore.common.fixture.CenterFixture.createCenter;
import static com.somemore.common.fixture.LocationFixture.createLocation;
import static com.somemore.common.fixture.RecruitBoardFixture.createRecruitBoard;
import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_CENTER;
import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_RECRUIT_BOARD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.somemore.IntegrationTestSupport;
import com.somemore.center.domain.Center;
import com.somemore.center.repository.CenterRepository;
import com.somemore.global.exception.BadRequestException;
import com.somemore.location.domain.Location;
import com.somemore.location.repository.LocationRepository;
import com.somemore.recruitboard.domain.RecruitBoard;
import com.somemore.recruitboard.dto.condition.RecruitBoardNearByCondition;
import com.somemore.recruitboard.dto.condition.RecruitBoardSearchCondition;
import com.somemore.recruitboard.dto.response.RecruitBoardDetailResponseDto;
import com.somemore.recruitboard.dto.response.RecruitBoardResponseDto;
import com.somemore.recruitboard.dto.response.RecruitBoardWithCenterResponseDto;
import com.somemore.recruitboard.dto.response.RecruitBoardWithLocationResponseDto;
import com.somemore.recruitboard.repository.RecruitBoardRepository;
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
    private CenterRepository centerRepository;

    @Autowired
    private LocationRepository locationRepository;

    private RecruitBoard recruitBoard;

    @BeforeEach
    void setUp() {
        recruitBoard = createRecruitBoard();
        recruitBoardRepository.save(recruitBoard);
    }

    @DisplayName("존재하는 ID가 주어지면 RecruitBoard 엔티티를 조회할 수 있다")
    @Test
    void getByIdWithExistsId() {
        // given
        Long id = recruitBoard.getId();

        // when
        RecruitBoardResponseDto dto = recruitBoardQueryService.getById(id);

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
            () -> recruitBoardQueryService.getById(wrongId)
        ).isInstanceOf(BadRequestException.class)
            .hasMessage(NOT_EXISTS_RECRUIT_BOARD.getMessage());
    }

    @DisplayName("아이디로 모집글과 기관를 조회할 수 있다.")
    @Test
    void getWithCenterById() {
        // given
        Location location = createLocation("특별한 주소");
        locationRepository.save(location);

        RecruitBoard board = createRecruitBoard(location.getId());
        recruitBoardRepository.save(board);

        // when
        RecruitBoardWithLocationResponseDto responseDto = recruitBoardQueryService.getWithLocationById(
            board.getId());

        // then
        assertThat(responseDto.id()).isEqualTo(board.getId());
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
            () -> recruitBoardQueryService.getWithLocationById(wrongId)
        ).isInstanceOf(BadRequestException.class)
            .hasMessage(NOT_EXISTS_RECRUIT_BOARD.getMessage());
    }

    @DisplayName("모집글과 기관 정보 리스트를 페이징 처리하여 받을 수 있다")
    @Test
    void getAllWithCenter() {
        // given
        String name = "특별한 기관";
        Center center = createCenter(name);
        centerRepository.save(center);

        RecruitBoard recruitBoard1 = createRecruitBoard(center.getId());
        recruitBoardRepository.save(recruitBoard1);

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
        assertThat(dtos.getContent().getFirst().center().name()).isEqualTo(name);
    }

    @DisplayName("위치 기반으로 주변 모집글을 페이징하여 조회할 수 있다")
    @Test
    void getRecruitBoardsNearBy() {
        // given
        Center center = createCenter();
        centerRepository.save(center);
        Location location = createLocation();
        locationRepository.save(location);

        RecruitBoard board = createRecruitBoard(center.getId(), location.getId());
        recruitBoardRepository.save(board);

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
        assertThat(result.getContent().getFirst().id()).isEqualTo(board.getId());
    }

    @DisplayName("기관 아이디로 모집글을 페이징하여 조회할 수 있다")
    @Test
    void getRecruitBoardsByCenterId() {
        // given
        Center center = createCenter("센터");
        centerRepository.save(center);
        RecruitBoard one = createRecruitBoard(center.getId());
        RecruitBoard two = createRecruitBoard(center.getId());
        RecruitBoard three = createRecruitBoard(center.getId());
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
        assertThat(result.getTotalElements()).isEqualTo(3);
    }

    @DisplayName("존재하지 않는 기관 아이디로 모집글을 조회할 경우 에러가 발생한다")
    @Test
    void getRecruitBoardsByCenterIdWhenNotExistsCenterId() {
        // given
        UUID wrongCenterId = UUID.randomUUID();
        RecruitBoardSearchCondition condition = RecruitBoardSearchCondition.builder()
            .build();

        // when
        // then
        assertThatThrownBy(
            () -> recruitBoardQueryService.getRecruitBoardsByCenterId(wrongCenterId, condition))
            .isInstanceOf(BadRequestException.class)
            .hasMessage(NOT_EXISTS_CENTER.getMessage());
    }

    private Pageable getPageable() {
        Sort sort = Sort.by(Sort.Order.desc("created_at"));
        return PageRequest.of(0, 5, sort);
    }
}
