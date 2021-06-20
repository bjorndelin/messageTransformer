package com.example.messagetransformer.exceptions;

import org.springframework.http.HttpStatus;

public enum ErrorType {
    BAD_FORMAT(HttpStatus.BAD_REQUEST),
    NOT_FOUND(HttpStatus.NOT_FOUND);

    private final HttpStatus httpStatus;

    ErrorType(final HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
