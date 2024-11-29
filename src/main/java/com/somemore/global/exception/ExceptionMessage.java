package com.somemore.global.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionMessage {

    NOT_EXISTS_CENTER("존재하지 않는 기관 입니다."),
    NOT_EXISTS_COMMUNITY_BOARD("존재하지 않는 게시글 입니다."),
    UNAUTHORIZED_COMMUNITY_BOARD("해당 게시글에 권한이 없습니다."),
    NOT_EXISTS_COMMUNITY_COMMENT("존재하지 않는 댓글 입니다."),
    UNAUTHORIZED_COMMUNITY_COMMENT("해당 댓글에 권한이 없습니다."),
    NOT_EXISTS_LOCATION("존재하지 않는 위치 ID 입니다."),
    NOT_EXISTS_RECRUIT_BOARD("존재하지 않는 봉사 모집글 ID 입니다."),
    UNAUTHORIZED_RECRUIT_BOARD("자신이 작성한 봉사 모집글이 아닙니다."),
    UPLOAD_FAILED("파일 업로드에 실패했습니다."),
    INVALID_FILE_TYPE("지원하지 않는 파일 형식입니다."),
    FILE_SIZE_EXCEEDED("파일 크기가 허용된 한도를 초과했습니다."),
    EMPTY_FILE("파일이 존재하지 않습니다."),
    INSTANTIATION_NOT_ALLOWED("인스턴스화 할 수 없는 클래스 입니다."),
    CANNOT_CANCEL_DELETED_INTEREST_CENTER("이미 삭제된 관심 기관은 취소할 수 없습니다."),
    DUPLICATE_INTEREST_CENTER("이미 관심 표시한 기관입니다.")
    ;

    private final String message;
}
