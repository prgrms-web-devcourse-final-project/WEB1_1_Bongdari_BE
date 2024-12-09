package com.somemore.community.service.board;

import com.somemore.IntegrationTestSupport;
import com.somemore.auth.oauth.OAuthProvider;
import com.somemore.community.domain.CommunityBoard;
import com.somemore.community.dto.response.CommunityBoardResponseDto;
import com.somemore.community.repository.board.CommunityBoardRepository;
import com.somemore.volunteer.domain.Volunteer;
import com.somemore.volunteer.repository.VolunteerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.UUID;

import static com.somemore.common.fixture.CommunityBoardFixture.createCommunityBoard;
import static org.assertj.core.api.Assertions.assertThat;

class CommunityBoardDocumentServiceTest extends IntegrationTestSupport {

    @Autowired
    private CommunityBoardDocumentService communityBoardDocumentService;

    @Autowired
    private CommunityBoardRepository communityBoardRepository;

    @Autowired
    private VolunteerRepository volunteerRepository;

    private UUID writerId;

    @BeforeEach
    void setUp() {
        String oAuthId1 = "example-oauth-id1";
        Volunteer volunteer1 = Volunteer.createDefault(OAuthProvider.NAVER, oAuthId1);
        volunteerRepository.save(volunteer1);
        writerId = volunteer1.getId();

        for (int i = 1; i <= 18; i++) {
            String title = "제목" + i;
            CommunityBoard communityBoard = createCommunityBoard(title, writerId);
            communityBoardRepository.save(communityBoard);
        }
    }

    @AfterEach
    void tearDown() {
        communityBoardRepository.deleteAllInBatch();
    }

    @DisplayName("검색 키워드가 포함된 게시글을 조회한다. (service)")
    @Test
    void getCommunityBoardBySearch() {
        //given
        //when
        Page<CommunityBoardResponseDto> dtos = communityBoardDocumentService.getCommunityBoardBySearch("봉사", 0);

        //then
        assertThat(dtos).isNotNull();
//        assertThat(dtos.getContent()).isNotNull();
//        assertThat(dtos.getTotalElements()).isEqualTo(10);
//        assertThat(dtos.getSize()).isEqualTo(10);
//        assertThat(dtos.getTotalPages()).isEqualTo(1);
    }

    @DisplayName("검색 키워드 없이 조회시 전체 게시글을 조회한다. (service)")
    @Test
    void getCommunityBoardBySearchWithNull() {
        //given
        //when
        Page<CommunityBoardResponseDto> dtos = communityBoardDocumentService.getCommunityBoardBySearch("", 0);

        //then
        assertThat(dtos).isNotNull();
        assertThat(dtos.getContent()).isNotNull();
//        assertThat(dtos.getTotalElements()).isEqualTo(17);
//        assertThat(dtos.getSize()).isEqualTo(10);
//        assertThat(dtos.getTotalPages()).isEqualTo(2);
    }
}
