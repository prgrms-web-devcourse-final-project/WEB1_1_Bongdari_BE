package com.somemore.global.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionMessage {

    NOT_EXISTS_CENTER("존재하지 않는 기관 ID 입니다."),
    NOT_EXISTS_COMMUNITY_BOARD("존재하지 않는 게시글 입니다."),
    UNAUTHORIZED_COMMUNITY_BOARD("해당 게시글에 권한이 없습니다."),
    NOT_EXISTS_LOCATION("존재하지 않는 위치 ID 입니다."),
    NOT_EXISTS_RECRUIT_BOARD("존재하지 않는 봉사 모집글 ID 입니다."),
    UNAUTHORIZED_RECRUIT_BOARD("자신이 작성한 봉사 모집글이 아닙니다."),
    ;

    private final String message;
}
