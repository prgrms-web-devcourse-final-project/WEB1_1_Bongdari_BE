package com.somemore.global.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidAuthenticationException extends AuthenticationException {

    public InvalidAuthenticationException(final String message) {
        super(message);
    }

    public InvalidAuthenticationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public InvalidAuthenticationException(final ExceptionMessage message) {
        super(message.getMessage());
    }

    public InvalidAuthenticationException(final ExceptionMessage message, final Throwable cause) {
        super(message.getMessage(), cause);
    }
}
