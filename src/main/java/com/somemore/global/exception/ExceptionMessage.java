package com.somemore.global.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionMessage {
    // USER
    NOT_EXIST_USER("존재하지 않는 유저입니다."),

    // CENTER
    NOT_EXISTS_CENTER("존재하지 않는 기관입니다."),
    NOT_EXISTS_PREFER_ITEM("존재하지 않는 선호물품 ID 입니다"),
    UNAUTHORIZED_PREFER_ITEM("해당 선호물품에 대한 권한이 없습니다."),

    // VOLUNTEER
    NOT_EXISTS_VOLUNTEER("존재하지 않는 봉사자입니다."),
    UNAUTHORIZED_VOLUNTEER_DETAIL("해당 봉사자의 상세 정보 조회 권한이 없습니다."),

    // COMMUNITY
    NOT_EXISTS_COMMUNITY_BOARD("존재하지 않는 게시글입니다."),
    UNAUTHORIZED_COMMUNITY_BOARD("해당 게시글에 권한이 없습니다."),
    NOT_EXISTS_COMMUNITY_COMMENT("존재하지 않는 댓글 입니다."),
    UNAUTHORIZED_COMMUNITY_COMMENT("해당 댓글에 권한이 없습니다."),

    // LOCATION
    NOT_EXISTS_LOCATION("존재하지 않는 위치 ID 입니다."),

    // RECRUIT BOARD
    NOT_EXISTS_RECRUIT_BOARD("존재하지 않는 봉사 모집글입니다."),
    UNAUTHORIZED_RECRUIT_BOARD("해당 봉사 모집글에 권한이 없습니다."),
    INVALID_RECRUIT_BOARD_TIME("종료 시간은 시작 시간보다 이후여야 합니다."),

    // IMAGE
    UPLOAD_FAILED("파일 업로드에 실패했습니다."),
    INVALID_FILE_TYPE("지원하지 않는 파일 형식입니다."),
    FILE_SIZE_EXCEEDED("파일 크기가 허용된 한도를 초과했습니다."),
    EMPTY_FILE("파일이 존재하지 않습니다."),
    INSTANTIATION_NOT_ALLOWED("인스턴스화 할 수 없는 클래스 입니다."),

    // INTEREST CENTER
    CANNOT_CANCEL_DELETED_INTEREST_CENTER("이미 삭제된 관심 기관은 취소할 수 없습니다."),
    DUPLICATE_INTEREST_CENTER("이미 관심 표시한 기관입니다."),

    // VOLUNTEER APPLY
    NOT_EXISTS_VOLUNTEER_APPLY("존재하지 않는 봉사 활동 지원입니다."),
    RECRUITMENT_NOT_OPEN("현재 모집 진행 중이 아닙니다."),
    DUPLICATE_APPLICATION("이미 신청한 봉사 모집 공고입니다."),
    UNAUTHORIZED_VOLUNTEER_APPLY("해당 지원에 권한이 없습니다."),
    RECRUIT_BOARD_ALREADY_COMPLETED("이미 종료된 봉사 활동입니다."),
    VOLUNTEER_APPLY_LIST_MISMATCH("봉사 지원 목록과 요청된 봉사 지원 목록이 일치하지 않습니다."),
    RECRUIT_BOARD_ID_MISMATCH("모든 봉사 신청이 동일한 모집글 ID를 가져야 합니다."),

    // REVIEW
    REVIEW_ALREADY_EXISTS("이미 작성한 리뷰가 존재합니다."),
    REVIEW_RESTRICTED_TO_ATTENDED("리뷰는 참석한 봉사에 한해서만 작성할 수 있습니다."),
    NOT_EXISTS_REVIEW("존재하지 않는 리뷰입니다."),
    UNAUTHORIZED_REVIEW("해당 리뷰에 권한이 없습니다."),

    // NOTIFICATION
    NOT_EXISTS_NOTIFICATION("존재하지 않는 알림입니다."),
    UNAUTHORIZED_NOTIFICATION("해당 알림에 권한이 없습니다."),

    // NOTE
    NOT_EXISTS_NOTE("존재하지 않는 쪽지입니다."),

    ;
    private final String message;
}
