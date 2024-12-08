package com.somemore.community.repository;

import com.somemore.IntegrationTestSupport;
import com.somemore.auth.oauth.OAuthProvider;
import com.somemore.community.domain.CommunityBoard;
import com.somemore.community.repository.board.CommunityBoardRepository;
import com.somemore.community.repository.mapper.CommunityBoardView;
import com.somemore.volunteer.domain.Volunteer;
import com.somemore.volunteer.repository.VolunteerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;


import static com.somemore.common.fixture.CommunityBoardFixture.createCommunityBoard;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
public class CommunityBoardDocumentRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private CommunityBoardRepository communityBoardRepository;
    @Autowired
    private VolunteerRepository volunteerRepository;

    @BeforeEach
    void setUp() {
        String oAuthId = "example-oauth-id2";
        Volunteer volunteer = Volunteer.createDefault(OAuthProvider.NAVER, oAuthId);
        volunteerRepository.save(volunteer);

        for (int i = 1; i <= 20; i++) {
            String title = "제목" + i;
            CommunityBoard communityBoard = createCommunityBoard(title, volunteer.getId());
            communityBoardRepository.save(communityBoard);
        }
    }

    @DisplayName("검색 키워드가 포함된 게시글을 조회할 수 있다. (repository)")
    @Test
    void findByCommunityBoardsContaining() {
        //given
        Pageable pageable = getPageable();

        //when
        Page<CommunityBoardView> findBoards = communityBoardRepository.findByCommunityBoardsContaining("봉사", pageable);

        //then
        assertThat(findBoards).isNotNull();
        assertThat(findBoards.getTotalElements()).isEqualTo(9);
        assertThat(findBoards.getSize()).isEqualTo(10);
        assertThat(findBoards.getTotalPages()).isEqualTo(1);
    }

//    @DisplayName("키워드 없이 검색시 전체 게시글을 조회할 수 있다. (repository)")
//    @Test
//    void findByCommunityBoardsContainingWithNull() {
//        //given
//        Pageable pageable = getPageable();
//
//        //when
//        Page<CommunityBoardView> findBoards = communityBoardRepository.findByCommunityBoardsContaining(null, pageable);
//
//        //then
//        assertThat(findBoards).isNotNull();
//        assertThat(findBoards.getTotalElements()).isEqualTo(16);
//        assertThat(findBoards.getSize()).isEqualTo(10);
//        assertThat(findBoards.getTotalPages()).isEqualTo(2);
//    }

    private Pageable getPageable() {
        return PageRequest.of(0, 10);
    }
}
