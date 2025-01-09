package com.somemore.domains.search.repository;

import com.somemore.domains.center.domain.Center;
import com.somemore.domains.center.repository.center.CenterRepository;
import com.somemore.domains.community.domain.CommunityBoard;
import com.somemore.domains.community.repository.board.CommunityBoardRepository;
import com.somemore.domains.community.repository.mapper.CommunityBoardView;
import com.somemore.domains.location.domain.Location;
import com.somemore.domains.location.repository.LocationRepository;
import com.somemore.domains.recruitboard.domain.RecruitBoard;
import com.somemore.domains.recruitboard.dto.condition.RecruitBoardNearByCondition;
import com.somemore.domains.recruitboard.dto.condition.RecruitBoardSearchCondition;
import com.somemore.domains.recruitboard.repository.RecruitBoardRepository;
import com.somemore.domains.recruitboard.repository.mapper.RecruitBoardDetail;
import com.somemore.domains.recruitboard.repository.mapper.RecruitBoardWithCenter;
import com.somemore.domains.volunteer.domain.Volunteer;
import com.somemore.domains.volunteer.repository.VolunteerRepository;
import com.somemore.global.auth.oauth.domain.OAuthProvider;
import com.somemore.support.IntegrationTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import org.springframework.transaction.annotation.Transactional;

import static com.somemore.support.fixture.CenterFixture.createCenter;
import static com.somemore.support.fixture.LocationFixture.createLocation;
import static com.somemore.support.fixture.RecruitBoardFixture.createRecruitBoard;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.somemore.support.fixture.CommunityBoardFixture.createCommunityBoard;

@Transactional
@EnabledIf(value = "${elastic.search.enabled}", loadContext = true)
class SearchBoardRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private CommunityBoardRepository communityBoardRepository;
    @Autowired
    private RecruitBoardRepository recruitBoardRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private CenterRepository centerRepository;
    @Autowired
    private SearchBoardRepository searchBoardRepository;
    @Autowired
    private VolunteerRepository volunteerRepository;

    private UUID writerId, centerId;
    private final List<RecruitBoard> boards = new ArrayList<>();

    @BeforeEach
    void setUp() {
        String oAuthId1 = "example-oauth-id1";
        Volunteer volunteer1 = Volunteer.createDefault(OAuthProvider.NAVER, oAuthId1);
        volunteerRepository.save(volunteer1);
        writerId = volunteer1.getId();

        for (int i = 1; i <= 20; i++) {
            String title = "제목" + i;
            CommunityBoard communityBoard = createCommunityBoard(title, writerId);
            communityBoardRepository.save(communityBoard);
        }

        Location location = createLocation();
        locationRepository.save(location);

        Center center = createCenter();
        centerRepository.save(center);
        centerId = center.getId();

        for (int i = 1; i <= 20; i++) {
            String title = "제목" + i;
            RecruitBoard board = createRecruitBoard(title, center.getId(), location.getId());
            boards.add(board);
        }
        recruitBoardRepository.saveAll(boards);
    }

    @DisplayName("커뮤니티 게시글을 elastic search index에 저장할 수 있다. (repository)")
    @Test
    void saveCommunityBoardDocuments() {
        //given
        Pageable pageable = getPageable(10);

        List<CommunityBoard> communityBoards = new ArrayList<>();

        CommunityBoard communityBoard1 = createCommunityBoard("저장 잘 되나요?", writerId);
        CommunityBoard savedBoard1 = communityBoardRepository.save(communityBoard1);
        CommunityBoard communityBoard2 = createCommunityBoard("잘 되나요?", "저장이요", writerId);
        CommunityBoard savedBoard2 = communityBoardRepository.save(communityBoard2);
        communityBoards.add(savedBoard1);
        communityBoards.add(savedBoard2);

        //when
        searchBoardRepository.saveCommunityBoardDocuments(communityBoards);

        //then
        Page<CommunityBoardView> findBoard = searchBoardRepository.findByCommunityBoardsContaining("저장", pageable);

        assertThat(findBoard).isNotNull();
        assertThat(findBoard.getTotalElements()).isEqualTo(2);

        searchBoardRepository.deleteCommunityBoardDocument(savedBoard1.getId());
        searchBoardRepository.deleteCommunityBoardDocument(savedBoard2.getId());
    }


    @DisplayName("검색 키워드가 포함된 게시글을 페이지로 조회할 수 있다. (elasticsearch)")
    @Test
    void findByCommunityBoardsContaining() {

        //given
        Pageable pageable = getPageable(10);

        String title = "봉사";
        CommunityBoard communityBoard = createCommunityBoard(title, writerId);
        communityBoardRepository.save(communityBoard);
        searchBoardRepository.saveCommunityBoardDocuments(List.of(communityBoard));

        //when
        Page<CommunityBoardView> communityBoards = searchBoardRepository.findByCommunityBoardsContaining("봉사", pageable);

        //then
        assertThat(communityBoards).isNotNull();
        assertThat(communityBoards.getTotalElements()).isEqualTo(1);
        assertThat(communityBoards.getSize()).isEqualTo(10);
        assertThat(communityBoards.getNumber()).isZero();
    }

    @DisplayName("키워드 없이 검색 시 전체 게시글을 페이지로 조회할 수 있다. (elasticsearch)")
    @Test
    void findByCommunityBoardsContainingWithNull() {

        //given
        Pageable pageable = getPageable(10);

        //when
        Page<CommunityBoardView> communityBoards = searchBoardRepository.findByCommunityBoardsContaining(null, pageable);

        //then
        assertThat(communityBoards).isNotNull();
        assertThat(communityBoards.getTotalElements()).isEqualTo(20);
        assertThat(communityBoards.getSize()).isEqualTo(10);
    }

    @DisplayName("봉사 활동 모집글을 elastic search index에 저장할 수 있다. (repository)")
    @Test
    void saveRecruitBoardDocuments() {
        //given
        Pageable pageable = getPageable(5);
        RecruitBoardSearchCondition condition = RecruitBoardSearchCondition.builder()
                .keyword("저장")
                .pageable(pageable)
                .build();

        List<RecruitBoard> recruitBoards = new ArrayList<>();

        RecruitBoard board1 = createRecruitBoard("저장 잘 되나요?", centerId);
        RecruitBoard savedBoard1 = recruitBoardRepository.save(board1);
        RecruitBoard board2 = createRecruitBoard("저장해줘", centerId);
        RecruitBoard savedBoard2 = recruitBoardRepository.save(board2);
        recruitBoards.add(savedBoard1);
        recruitBoards.add(savedBoard2);

        //when
        searchBoardRepository.saveRecruitBoardDocuments(recruitBoards);

        //then
        Page<RecruitBoardWithCenter> findBoard = searchBoardRepository.findByRecruitBoardsContaining(condition);

        assertThat(findBoard).isNotNull();
        assertThat(findBoard.getTotalElements()).isEqualTo(2);

        searchBoardRepository.deleteRecruitBoardDocument(savedBoard1.getId());
        searchBoardRepository.deleteRecruitBoardDocument(savedBoard2.getId());
    }

    @DisplayName("검색 키워드가 포함된 모집글을 조회할 수 있다. (elasticsearch)")
    @Test
    void findByRecruitBoardsContaining() {
        //given
        Pageable pageable = getPageable(5);
        RecruitBoardSearchCondition condition = RecruitBoardSearchCondition.builder()
                .keyword("노인")
                .pageable(pageable)
                .build();

        //when
        Page<RecruitBoardWithCenter> findBoards = searchBoardRepository.findByRecruitBoardsContaining(condition);

        //then
        assertThat(findBoards).isNotNull();
        assertThat(findBoards.getTotalElements()).isEqualTo(4);
        assertThat(findBoards.getSize()).isEqualTo(5);
        assertThat(findBoards.getTotalPages()).isEqualTo(1);
    }

    @DisplayName("키워드 없이 검색 시 전체 모집글을 조회할 수 있다. (elasticsearch)")
    @Test
    void findByRecruitBoardsContainingWithNull() {
        //given
        Pageable pageable = getPageable(5);
        RecruitBoardSearchCondition condition = RecruitBoardSearchCondition.builder()
                .keyword(null)
                .pageable(pageable)
                .build();

        //when
        Page<RecruitBoardWithCenter> findBoards = searchBoardRepository.findByRecruitBoardsContaining(condition);

        //then
        assertThat(findBoards).isNotNull();
        assertThat(findBoards.getTotalElements()).isEqualTo(20);
        assertThat(findBoards.getSize()).isEqualTo(5);
        assertThat(findBoards.getTotalPages()).isEqualTo(4);
    }

    @DisplayName("위치 기반으로 반경 내에 검색 키워드가 포함된 모집글을 조회할 수 있다. (elasticsearch)")
    @Test
    void findAllNearByLocationContaining() {
        // given
        Pageable pageable = getPageable(5);

        RecruitBoardNearByCondition condition = RecruitBoardNearByCondition.builder()
                .keyword("봉사")
                .latitude(37.5935)
                .longitude(126.9780)
                .radius(5.0)
                .pageable(pageable)
                .build();

        // when
        Page<RecruitBoardDetail> result = searchBoardRepository.findAllNearbyWithKeyword(condition);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(17);
        assertThat(result.getContent()).isNotEmpty();
    }

    @DisplayName("키워드 없이 검색 시 위치 기반으로 반경 내에 모집글을 모두 조회할 수 있다. (elasticsearch)")
    @Test
    void findAllNearByLocationContainingWithNull() {
        // given
        Pageable pageable = getPageable(5);

        RecruitBoardNearByCondition condition = RecruitBoardNearByCondition.builder()
                .keyword(null)
                .latitude(37.5935)
                .longitude(126.9780)
                .radius(5.0)
                .pageable(pageable)
                .build();

        // when
        Page<RecruitBoardDetail> result = searchBoardRepository.findAllNearbyWithKeyword(condition);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(20);
        assertThat(result.getContent()).isNotEmpty();
    }

    private Pageable getPageable(int size) {
        return PageRequest.of(0, size);
    }
}
