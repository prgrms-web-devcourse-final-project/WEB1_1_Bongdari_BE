package com.somemore.community.service.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.somemore.support.IntegrationTestSupport;
import com.somemore.community.dto.request.CommunityBoardCreateRequestDto;
import com.somemore.community.repository.board.CommunityBoardRepository;
import com.somemore.community.usecase.board.CreateCommunityBoardUseCase;
import com.somemore.community.usecase.board.CommunityBoardQueryUseCase;
import com.somemore.global.exception.BadRequestException;
import com.somemore.global.exception.ExceptionMessage;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

class DeleteCommunityBoardServiceTest extends IntegrationTestSupport {
    @Autowired
    private DeleteCommunityBoardService deleteCommunityBoardService;
    @Autowired
    private CreateCommunityBoardUseCase createCommunityBoardUseCase;
    @Autowired
    private CommunityBoardQueryUseCase communityBoardQueryUseCase;
    @Autowired
    private CommunityBoardRepository communityBoardRepository;

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

    @DisplayName("커뮤니티 게시글 id로 게시글을 삭제한다.")
    @Test
    void deleteCommunityBoardWithId() {
        //given
        //when
        deleteCommunityBoardService.deleteCommunityBoard(writerId, communityId);

        //then
        assertThat(communityBoardQueryUseCase.getCommunityBoards(null, 0)).isEmpty();
    }

    @DisplayName("삭제된 커뮤니티 게시글의 id로 게시글을 삭제할 때 예외를 던진다.")
    @Test
    void deleteCommunityBoardWithDeletedId() {
        //given
        deleteCommunityBoardService.deleteCommunityBoard(writerId, communityId);

        //when
        ThrowableAssert.ThrowingCallable callable = () -> deleteCommunityBoardService.deleteCommunityBoard(writerId, communityId);

        //then
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(callable)
                .withMessage(ExceptionMessage.NOT_EXISTS_COMMUNITY_BOARD.getMessage());
    }

    @DisplayName("작성자가 아닌 id로 커뮤니티 게시글을 삭제하고자 할 때 예외를 던진다.")
    @Test
    void deleteCommunityBoardWithNotWriterId() {
        //given
        //when
        ThrowableAssert.ThrowingCallable callable = () -> deleteCommunityBoardService.deleteCommunityBoard(UUID.randomUUID(), communityId);

        //then
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(callable)
                .withMessage(ExceptionMessage.UNAUTHORIZED_COMMUNITY_BOARD.getMessage());
    }
}
