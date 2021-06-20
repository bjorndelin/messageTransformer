package com.example.messagetransformer.exceptions;

import lombok.Data;

@Data
public class MessageTransformerException extends Exception {

    private String messaga;
    private ErrorType errorType;

    public MessageTransformerException(final String message, final ErrorType errorType) {
        this.messaga = message;
        this.errorType = errorType;
    }
}
