package com.somemore.global.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionMessage {

    NOT_EXISTS_CENTER("존재하지 않는 기관 ID 입니다.")
    ;

    private final String message;
}
