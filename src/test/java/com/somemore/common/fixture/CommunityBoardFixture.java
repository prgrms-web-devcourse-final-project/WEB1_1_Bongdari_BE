package com.somemore.common.fixture;

import com.somemore.community.domain.CommunityBoard;

import java.util.UUID;

public class CommunityBoardFixture {

    public static final String TITLE = "테스트 커뮤니티 게시글 제목";
    public static final String CONTENT = "테스트 커뮤니티 게시글 내용";
    public static final String IMG_URL = "http://community.example.com/123";

    private CommunityBoardFixture() {

    }

    public static CommunityBoard createCommunityBoard() {
        return CommunityBoard.builder()
                .title(TITLE)
                .content(CONTENT)
                .imgUrl(IMG_URL)
                .writerId(UUID.randomUUID())
                .build();
    }

    public static CommunityBoard createCommunityBoard(UUID writerId) {
        return CommunityBoard.builder()
                .title(TITLE)
                .content(CONTENT)
                .imgUrl(IMG_URL)
                .writerId(writerId)
                .build();
    }
    public static CommunityBoard createCommunityBoard(String title, UUID writerId) {
        return CommunityBoard.builder()
                .title(title)
                .content(CONTENT)
                .imgUrl(IMG_URL)
                .writerId(writerId)
                .build();
    }
}
