package com.somemore.domains.recruitboard.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RecruitStatus {
    RECRUITING("모집중", true),
    CLOSED("마감", true),
    COMPLETED("종료", false),

    ;
    private final String text;
    private final boolean changeable;
}
