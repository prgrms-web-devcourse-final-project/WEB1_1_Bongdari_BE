package com.somemore.recruitboard.service.query;

import com.somemore.IntegrationTestSupport;
import com.somemore.center.domain.Center;
import com.somemore.center.repository.center.CenterRepository;
import com.somemore.location.domain.Location;
import com.somemore.location.repository.LocationRepository;
import com.somemore.recruitboard.domain.RecruitBoard;
import com.somemore.recruitboard.dto.condition.RecruitBoardSearchCondition;
import com.somemore.recruitboard.dto.response.RecruitBoardWithCenterResponseDto;
import com.somemore.recruitboard.repository.RecruitBoardRepository;
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
public class RecruitBoardDocumentServiceTest extends IntegrationTestSupport {

    @Autowired
    private RecruitBoardDocumentService recruitBoardDocumentService;

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

    @DisplayName("검색 키워드가 포함된 모집글을 조회한다. (service)")
    @Test
    void getRecruitBoardBySearch() {
        //given
        Pageable pageable = getPageable();
        RecruitBoardSearchCondition condition = RecruitBoardSearchCondition.builder()
                .pageable(pageable)
                .build();

        //when
        Page<RecruitBoardWithCenterResponseDto> dtos = recruitBoardDocumentService.getRecruitBoardBySearch("노인", condition);

        //then
        assertThat(dtos).isNotNull();
        assertThat(dtos.getContent()).isNotNull();
        assertThat(dtos.getTotalElements()).isEqualTo(4);
        assertThat(dtos.getSize()).isEqualTo(5);
        assertThat(dtos.getTotalPages()).isEqualTo(1);
    }

    private Pageable getPageable() {
        Sort sort = Sort.by(Sort.Order.desc("created_at"));
        return PageRequest.of(0, 5, sort);
    }
}
