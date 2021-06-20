package com.example.messagetransformer.models.responses;

import lombok.Data;

@Data
public class ErrorMessageResponse {

    private String error;

    public ErrorMessageResponse(final String error) {
        this.error = error;
    }
}
