package com.somemore.community.repository.mapper;

import com.somemore.community.domain.CommunityBoard;

public record CommunityBoardView(
        CommunityBoard communityBoard,
        String writerNickname) {
}
