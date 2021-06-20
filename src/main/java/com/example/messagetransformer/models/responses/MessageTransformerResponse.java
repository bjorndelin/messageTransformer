package com.example.messagetransformer.models.responses;

import com.example.messagetransformer.models.MessageData;
import lombok.Data;

@Data
public class MessageTransformerResponse {
    private String result;

    public static MessageTransformerResponse convert(final MessageData messageData) {
        final MessageTransformerResponse response = new MessageTransformerResponse();
        response.setResult(messageData.getConvertedString());
        return response;
    }
}
