package com.somemore.community.service.command;

import static org.assertj.core.api.Assertions.assertThat;

import com.somemore.IntegrationTestSupport;
import com.somemore.community.domain.CommunityBoard;
import com.somemore.community.dto.request.CommunityBoardCreateRequestDto;
import com.somemore.community.repository.CommunityBoardRepository;
import com.somemore.community.service.command.command.CreateCommunityBoardService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.UUID;

public class CreateCommunityBoardServiceTest extends IntegrationTestSupport {
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
        String imgUrl = "https://image.test.url/123";

        //when
        Long communityId = createCommunityBoardService.createCommunityBoard(dto, writerId, imgUrl);

        //then
        Optional<CommunityBoard> communityBoard = communityBoardRepository.findById(communityId);

        assertThat(communityBoard.isPresent()).isTrue();
        assertThat(communityBoard.get().getId()).isEqualTo(communityId);
        assertThat(communityBoard.get().getWriterId()).isEqualTo(writerId);
        assertThat(communityBoard.get().getTitle()).isEqualTo(dto.title());
        assertThat(communityBoard.get().getContent()).isEqualTo(dto.content());
        assertThat(communityBoard.get().getImgUrl()).isEqualTo(imgUrl);
    }

    @DisplayName("커뮤니티 게시글을 이미지 링크 없이 등록할 시 빈 문자열을 저장한다.")
    @Test
    void createCommunityWithoutImgUrl() {
        //given
        CommunityBoardCreateRequestDto dto = CommunityBoardCreateRequestDto.builder()
                .title("커뮤니티 테스트 제목")
                .content("커뮤니티 테스트 내용")
                .build();

        UUID writerId = UUID.randomUUID();
        String imgUrl = null;

        //when
        Long communityId = createCommunityBoardService.createCommunityBoard(dto, writerId, imgUrl);

        //then
        Optional<CommunityBoard> communityBoard = communityBoardRepository.findById(communityId);

        assertThat(communityBoard.isPresent()).isTrue();
        assertThat(communityBoard.get().getId()).isEqualTo(communityId);
        assertThat(communityBoard.get().getWriterId()).isEqualTo(writerId);
        assertThat(communityBoard.get().getTitle()).isEqualTo(dto.title());
        assertThat(communityBoard.get().getContent()).isEqualTo(dto.content());
        assertThat(communityBoard.get().getImgUrl()).isEqualTo("");
    }
}
