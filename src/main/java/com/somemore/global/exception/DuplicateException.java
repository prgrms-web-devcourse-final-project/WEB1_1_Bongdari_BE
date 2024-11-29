package com.somemore.global.exception;

public class DuplicateException extends RuntimeException{

    public DuplicateException(final String message) {
        super(message);
    }
}
