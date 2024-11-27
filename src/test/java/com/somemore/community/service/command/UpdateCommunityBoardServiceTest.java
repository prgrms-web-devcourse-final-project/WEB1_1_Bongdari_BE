package com.somemore.community.service.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.somemore.IntegrationTestSupport;
import com.somemore.community.domain.CommunityBoard;
import com.somemore.community.dto.request.CommunityBoardCreateRequestDto;
import com.somemore.community.dto.request.CommunityBoardUpdateRequestDto;
import com.somemore.community.repository.CommunityBoardRepository;
import com.somemore.community.usecase.command.CreateCommunityBoardUseCase;
import com.somemore.global.exception.BadRequestException;
import com.somemore.global.exception.ExceptionMessage;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.UUID;

class UpdateCommunityBoardServiceTest extends IntegrationTestSupport {

    @Autowired
    private CreateCommunityBoardUseCase createCommunityBoardUseCase;
    @Autowired
    private CommunityBoardRepository communityBoardRepository;
    @Autowired
    private UpdateCommunityBoardService updateCommunityBoardService;

    private UUID writerId;
    private Long communityId;
    private String imgUrl;

    @BeforeEach
    void setUp() {
        CommunityBoardCreateRequestDto dto = CommunityBoardCreateRequestDto.builder()
                .title("커뮤니티 테스트 제목")
                .content("커뮤니티 테스트 내용")
                .build();

        writerId = UUID.randomUUID();
        imgUrl = "https://image.test.url/123";

        communityId = createCommunityBoardUseCase.createCommunityBoard(dto, writerId, imgUrl);
    }

    @AfterEach
    void tearDown() {
        communityBoardRepository.deleteAllInBatch();
    }

    @DisplayName("커뮤니티 게시글을 수정한다.")
    @Test
    void updateCommunityBoard() {

        //given
        CommunityBoardUpdateRequestDto dto = CommunityBoardUpdateRequestDto.builder()
                .title("수정된 커뮤니티 테스트 제목")
                .content("수정된 커뮤니티 테스트 내용")
                .build();

        String newImgUrl = "https://image.test.url/567";

        //when
        updateCommunityBoardService.updateCommunityBoard(dto, communityId, writerId, newImgUrl);

        //then
        Optional<CommunityBoard> updatedCommunityBoard = communityBoardRepository.findById(communityId);
        assertThat(updatedCommunityBoard).isNotNull();
        assertThat(updatedCommunityBoard.get().getId()).isEqualTo(communityId);
        assertThat(updatedCommunityBoard.get().getWriterId()).isEqualTo(writerId);
        assertThat(updatedCommunityBoard.get().getTitle()).isEqualTo("수정된 커뮤니티 테스트 제목");
        assertThat(updatedCommunityBoard.get().getContent()).isEqualTo("수정된 커뮤니티 테스트 내용");
        assertThat(updatedCommunityBoard.get().getImgUrl()).isEqualTo("https://image.test.url/567");
    }

    @DisplayName("작성자가 아닌 id로 커뮤니티 게시글을 수정하고자 할 때 예외를 던진다.")
    @Test
    void updateCommunityBoardNotWriterId() {

        //given
        CommunityBoardUpdateRequestDto dto = CommunityBoardUpdateRequestDto.builder()
                .title("수정된 커뮤니티 테스트 제목")
                .content("수정된 커뮤니티 테스트 내용")
                .build();

        //when
        ThrowableAssert.ThrowingCallable callable = () -> updateCommunityBoardService.updateCommunityBoard(dto, communityId, UUID.randomUUID(), null);

        //then
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(callable)
                .withMessage(ExceptionMessage.UNAUTHORIZED_COMMUNITY_BOARD.getMessage());
    }
}
