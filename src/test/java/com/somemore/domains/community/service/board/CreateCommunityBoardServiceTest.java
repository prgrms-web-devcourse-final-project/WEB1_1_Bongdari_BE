package com.somemore.domains.community.service.board;

import com.somemore.domains.community.domain.CommunityBoard;
import com.somemore.domains.community.dto.request.CommunityBoardCreateRequestDto;
import com.somemore.domains.community.repository.board.CommunityBoardRepository;
import com.somemore.support.IntegrationTestSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CreateCommunityBoardServiceTest extends IntegrationTestSupport {
    @Autowired
    private CreateCommunityBoardService createCommunityBoardService;
    @Autowired
    private CommunityBoardRepository communityBoardRepository;

    @AfterEach
    void tearDown() {
        communityBoardRepository.deleteAllInBatch();
    }

    @DisplayName("커뮤니티에 게시글을 등록한다.")
    @Test
    void createCommunityWithDto() {
        //given
        CommunityBoardCreateRequestDto dto = CommunityBoardCreateRequestDto.builder()
                .title("커뮤니티 테스트 제목")
                .content("커뮤니티 테스트 내용")
                .build();

        UUID writerId = UUID.randomUUID();

        //when
        Long communityId = createCommunityBoardService.createCommunityBoard(dto, writerId);

        //then
        Optional<CommunityBoard> communityBoard = communityBoardRepository.findById(communityId);

        assertThat(communityBoard).isPresent();
        assertThat(communityBoard.get().getId()).isEqualTo(communityId);
        assertThat(communityBoard.get().getWriterId()).isEqualTo(writerId);
        assertThat(communityBoard.get().getTitle()).isEqualTo(dto.title());
        assertThat(communityBoard.get().getContent()).isEqualTo(dto.content());
    }
}
