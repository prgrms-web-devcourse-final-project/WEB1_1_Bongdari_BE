package com.somemore.recruitboard.service.query;

import static com.somemore.common.fixture.CenterFixture.createCenter;
import static com.somemore.common.fixture.LocalDateTimeFixture.createCurrentDateTime;
import static com.somemore.common.fixture.LocationFixture.createLocation;
import static com.somemore.common.fixture.RecruitBoardFixture.createCompletedRecruitBoard;
import static com.somemore.common.fixture.RecruitBoardFixture.createRecruitBoard;
import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_CENTER;
import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_RECRUIT_BOARD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.somemore.IntegrationTestSupport;
import com.somemore.center.domain.Center;
import com.somemore.center.repository.center.CenterRepository;
import com.somemore.global.exception.BadRequestException;
import com.somemore.location.domain.Location;
import com.somemore.location.repository.LocationRepository;
import com.somemore.recruitboard.domain.RecruitBoard;
import com.somemore.recruitboard.domain.RecruitStatus;
import com.somemore.recruitboard.dto.condition.RecruitBoardNearByCondition;
import com.somemore.recruitboard.dto.condition.RecruitBoardSearchCondition;
import com.somemore.recruitboard.dto.response.RecruitBoardDetailResponseDto;
import com.somemore.recruitboard.dto.response.RecruitBoardResponseDto;
import com.somemore.recruitboard.dto.response.RecruitBoardWithCenterResponseDto;
import com.somemore.recruitboard.dto.response.RecruitBoardWithLocationResponseDto;
import com.somemore.recruitboard.repository.RecruitBoardRepository;

import java.util.ArrayList;
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
    private RecruitBoardDocumentService recruitBoardDocumentService;

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
                () -> recruitBoardQueryService.getRecruitBoardById(wrongId)
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
        List<Long> notCompletedBoardIds = recruitBoardQueryService.getNotCompletedIdsByCenterIds(
                centerId);

        // then
        assertThat(notCompletedBoardIds)
                .hasSize(2);

        assertThat(notCompletedBoardIds)
                .doesNotContain(deletedRecruitingBoard.getId())
                .doesNotContain(deletedClosedBoard.getId())
                .doesNotContain(deletedCompletedRecruitBoard.getId())
                .doesNotContain(completedRecruitBoard.getId());
    }

    @DisplayName("아이디리스트로 모집글 리스트를 조회할 수 있다.")
    @Test
    void getAllByIds() {
        // given
        RecruitBoard board1 = createRecruitBoard();
        RecruitBoard board2 = createRecruitBoard();
        RecruitBoard board3 = createRecruitBoard();

        recruitBoardRepository.saveAll(List.of(board1, board2, board3));
        List<Long> ids = List.of(board1.getId(), board2.getId(), board3.getId(), 100000L);

        // when
        List<RecruitBoard> all = recruitBoardQueryService.getAllByIds(ids);

        // then
        assertThat(all).hasSize(3);
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

        recruitBoardRepository.deleteDocument(savedBoard1.getId());
        recruitBoardRepository.deleteDocument(savedBoard2.getId());
    }

    private Pageable getPageable() {
        Sort sort = Sort.by(Sort.Order.desc("created_at"));
        return PageRequest.of(0, 5, sort);
    }
}
