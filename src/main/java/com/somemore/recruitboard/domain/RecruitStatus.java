package com.somemore.recruitboard.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RecruitStatus {
    RECRUITING("모집중"),
    CLOSED("마감"),
    COMPLETED("종료"),

    ;
    private final String text;

}
