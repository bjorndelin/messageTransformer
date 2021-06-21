package com.example.messagetransformer.models;

import lombok.Data;

@Data
public class MessageData {
    private String originalString;
    private MessageDataType messageDataType;
    private String convertedString;
}
