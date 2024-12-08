package com.somemore.global.exception;

public class NoSuchElementException extends RuntimeException{
    public NoSuchElementException(final ExceptionMessage message) {
        super(message.getMessage());
    }
}
