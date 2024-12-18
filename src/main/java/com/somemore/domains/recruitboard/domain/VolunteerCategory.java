package com.somemore.domains.recruitboard.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VolunteerCategory {

    LIVING_SUPPORT("생활편의지원"),
    HOUSING_ENVIRONMENT("주거환경"),
    COUNSELING("상담"),
    EDUCATION("교육"),
    HEALTHCARE("보건의료"),
    RURAL_SUPPORT("농어촌봉사"),
    CULTURAL_EVENT("문화행사"),
    ENVIRONMENTAL_PROTECTION("환경보호"),
    ADMINISTRATIVE_SUPPORT("행정보조"),
    SAFETY_PREVENTION("안전예방"),
    PUBLIC_INTEREST_HUMAN_RIGHTS("공익인권"),
    DISASTER_RELIEF("재해재난"),
    MENTORING("멘토링"),
    OTHER("기타"),

    ;
    private final String text;
}
