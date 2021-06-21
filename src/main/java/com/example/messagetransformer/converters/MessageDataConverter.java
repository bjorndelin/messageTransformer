package com.example.messagetransformer.converters;

import com.example.messagetransformer.db.model.MessageDataEntity;
import com.example.messagetransformer.exceptions.ErrorType;
import com.example.messagetransformer.exceptions.MessageTransformerException;
import com.example.messagetransformer.models.MessageData;
import com.example.messagetransformer.models.MessageDataType;
import com.example.messagetransformer.models.requests.MessageTransformerRequest;
import com.example.messagetransformer.models.responses.MessageTransformerResponse;

import java.time.LocalDateTime;

public class MessageDataConverter {
    public static MessageData convertToMessageData(final MessageTransformerRequest request) throws MessageTransformerException {
        final MessageData messageData = new MessageData();
        try {
            messageData.setOriginalString(request.getMessage());
            messageData.setMessageDataType(MessageDataType.valueOf(request.getMessageDataType()));
        } catch (Exception e) {
            throw new MessageTransformerException("Bad request", ErrorType.BAD_FORMAT);
        }

        return messageData;
    }

    public static MessageTransformerResponse convertToMessageTransformerResponse(final MessageData messageData) {
        final MessageTransformerResponse response = new MessageTransformerResponse();
        response.setResult(messageData.getConvertedString());
        return response;
    }

    public static MessageDataEntity convertToMessageDataEntity(final MessageData messageData) {
        final MessageDataEntity entity = new MessageDataEntity();
        entity.setMessageDataType(messageData.getMessageDataType());
        entity.setTransformedValue(messageData.getConvertedString());
        entity.setOriginalValue(messageData.getOriginalString());
        entity.setCreated(LocalDateTime.now());
        entity.setModified(entity.getModified());
        entity.setCounter(1L);
        return entity;
    }
}
