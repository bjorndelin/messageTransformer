package com.example.messagetransformer.models.responses;

import lombok.Value;

@Value
public class ErrorMessageResponse {

    String error;

    public ErrorMessageResponse(final String error) {
        this.error = error;
    }
}
