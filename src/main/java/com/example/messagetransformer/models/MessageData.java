package com.example.messagetransformer.models;

import com.example.messagetransformer.exceptions.ErrorType;
import com.example.messagetransformer.exceptions.MessageTransformerException;
import com.example.messagetransformer.models.requests.MessageTransformerRequest;
import lombok.Data;

@Data
public class MessageData {
    private String originalString;
    private MessageDataType messageDataType;
    private String convertedString;

    public static MessageData convert(final MessageTransformerRequest request) throws MessageTransformerException {
        final MessageData messageData = new MessageData();
        try {
            messageData.setOriginalString(request.getMessage());
            messageData.setMessageDataType(MessageDataType.valueOf(request.getMessageDataType()));
        } catch (Exception e) {
            throw new MessageTransformerException("Bad request", ErrorType.BAD_FORMAT);
        }

        return messageData;
    }
}
