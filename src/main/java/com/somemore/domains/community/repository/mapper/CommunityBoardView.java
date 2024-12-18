package com.somemore.domains.community.repository.mapper;

import com.somemore.domains.community.domain.CommunityBoard;

public record CommunityBoardView(
        CommunityBoard communityBoard,
        String writerNickname) {
}
