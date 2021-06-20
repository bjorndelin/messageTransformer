package com.example.messagetransformer.transformers;

import com.example.messagetransformer.exceptions.MessageTransformerException;
import com.example.messagetransformer.models.MessageData;
import com.example.messagetransformer.models.MessageDataType;
import org.springframework.stereotype.Component;

@Component
public interface MessageTransformerInterface {
    MessageData transform(final MessageData message) throws MessageTransformerException;
    MessageDataType getMessageDataType();
}
