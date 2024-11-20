package com.somemore.community.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.somemore.community.domain.CommunityBoard;
import com.somemore.community.dto.request.CommunityCreateRequestDto;
import com.somemore.community.repository.CommunityRepository;
import com.somemore.community.service.command.CreateCommunityService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

@ActiveProfiles("test")
@EnableJpaAuditing
@SpringBootTest
public class CreateCommunityServiceTest {
    @Autowired
    private CreateCommunityService createCommunityService;
    @Autowired
    private CommunityRepository communityRepository;

    @DisplayName("커뮤니티에 게시글을 등록한다.")
    @Test
    void createCommunityWithDto() {
        //given
        CommunityCreateRequestDto dto = CommunityCreateRequestDto.builder()
                .title("커뮤니티 테스트 제목")
                .content("커뮤니티 테스트 내용")
                .build();

        UUID writerId = UUID.randomUUID();
        String imgUrl = "https://image.test.url/123";

        //when
        Long communityId = createCommunityService.createCommunityBoard(dto, writerId, imgUrl);

        //then
        Optional<CommunityBoard> communityBoard = communityRepository.findById(communityId);

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
        CommunityCreateRequestDto dto = CommunityCreateRequestDto.builder()
                .title("커뮤니티 테스트 제목")
                .content("커뮤니티 테스트 내용")
                .build();

        UUID writerId = UUID.randomUUID();
        String imgUrl = null;

        //when
        Long communityId = createCommunityService.createCommunityBoard(dto, writerId, imgUrl);

        //then
        Optional<CommunityBoard> communityBoard = communityRepository.findById(communityId);

        assertThat(communityBoard.isPresent()).isTrue();
        assertThat(communityBoard.get().getId()).isEqualTo(communityId);
        assertThat(communityBoard.get().getWriterId()).isEqualTo(writerId);
        assertThat(communityBoard.get().getTitle()).isEqualTo(dto.title());
        assertThat(communityBoard.get().getContent()).isEqualTo(dto.content());
        assertThat(communityBoard.get().getImgUrl()).isEqualTo("");
    }
}
