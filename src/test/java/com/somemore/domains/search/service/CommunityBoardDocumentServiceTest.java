package com.somemore.domains.search.service;

import com.somemore.domains.community.domain.CommunityBoard;
import com.somemore.domains.community.dto.response.CommunityBoardResponseDto;
import com.somemore.domains.community.repository.board.CommunityBoardRepository;
import com.somemore.domains.search.repository.SearchBoardRepository;
import com.somemore.domains.volunteer.domain.Volunteer;
import com.somemore.domains.volunteer.repository.VolunteerRepository;
import com.somemore.global.auth.oauth.OAuthProvider;
import com.somemore.support.IntegrationTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.somemore.support.fixture.CommunityBoardFixture.createCommunityBoard;

@Transactional
@EnabledIf(value = "${elastic.search.enabled}", loadContext = true)
class CommunityBoardDocumentServiceTest extends IntegrationTestSupport {

    @Autowired
    private CommunityBoardDocumentService communityBoardDocumentService;

    @Autowired
    private CommunityBoardRepository communityBoardRepository;

    @Autowired
    private SearchBoardRepository searchBoardRepository;

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

    @DisplayName("검색 키워드가 포함된 게시글을 조회한다. (ealsticsearch)")
    @Test
    void getCommunityBoardBySearch() {
        //given
        //when
        Page<CommunityBoardResponseDto> dtos = communityBoardDocumentService.getCommunityBoardBySearch("봉사", 0);

        //then
        assertThat(dtos).isNotNull();
        assertThat(dtos.getContent()).isNotNull();
        assertThat(dtos.getTotalElements()).isEqualTo(10);
        assertThat(dtos.getSize()).isEqualTo(10);
        assertThat(dtos.getTotalPages()).isEqualTo(1);
    }

    @DisplayName("검색 키워드 없이 조회시 전체 게시글을 조회한다. (elasticsearch)")
    @Test
    void getCommunityBoardBySearchWithNull() {
        //given
        //when
        Page<CommunityBoardResponseDto> dtos = communityBoardDocumentService.getCommunityBoardBySearch("", 0);

        //then
        assertThat(dtos).isNotNull();
        assertThat(dtos.getContent()).isNotNull();
        assertThat(dtos.getTotalElements()).isEqualTo(18);
        assertThat(dtos.getSize()).isEqualTo(10);
        assertThat(dtos.getTotalPages()).isEqualTo(2);
    }

    @DisplayName("게시글을 elastic search index에 저장한다. (service)")
    @Test
    void saveCommunityBoardDocuments() {
        //given
        List<CommunityBoard> communityBoards = new ArrayList<>();

        CommunityBoard communityBoard = createCommunityBoard("저장 잘 되나요?", "안녕하세요",  UUID.randomUUID());
        CommunityBoard savedBoard = communityBoardRepository.save(communityBoard);
        communityBoards.add(savedBoard);

        //when
        communityBoardDocumentService.saveCommunityBoardDocuments(communityBoards);

        //then
        Page<CommunityBoardResponseDto> dtos = communityBoardDocumentService.getCommunityBoardBySearch("", 0);

        assertThat(dtos).isNotNull();
        assertThat(dtos.getContent()).isNotNull();
        assertThat(dtos.getTotalElements()).isEqualTo(18);
        assertThat(dtos.getSize()).isEqualTo(10);
        assertThat(dtos.getTotalPages()).isEqualTo(2);

        searchBoardRepository.deleteCommunityBoardDocument(savedBoard.getId());
    }
}
