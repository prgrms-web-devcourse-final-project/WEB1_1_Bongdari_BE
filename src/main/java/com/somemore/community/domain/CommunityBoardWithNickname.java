package com.somemore.community.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommunityBoardWithNickname {
    private final CommunityBoard communityBoard;
    private final String writerNickname;
}
