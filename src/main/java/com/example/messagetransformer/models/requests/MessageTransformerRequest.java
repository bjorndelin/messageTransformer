package com.example.messagetransformer.models.requests;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class MessageTransformerRequest {
    @NotNull
    @Size(min = 1, max = 1000)
    private String message;
    @NotNull
    @Size(min = 1, max = 30)
    private String messageDataType;
}
