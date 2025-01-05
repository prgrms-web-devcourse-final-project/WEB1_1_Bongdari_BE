package com.somemore.global.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class LoggedResponse {
    private final HttpStatusCode statusCode;
    private final String body;

    public LoggedResponse(HttpStatusCode statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }
}
