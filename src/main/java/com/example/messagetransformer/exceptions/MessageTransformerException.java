package com.example.messagetransformer.exceptions;

import lombok.Data;

@Data
public class MessageTransformerException extends Exception {

    private final String message;
    private final ErrorType errorType;

    public MessageTransformerException(final String message, final ErrorType errorType) {
        this.message = message;
        this.errorType = errorType;
    }
}
