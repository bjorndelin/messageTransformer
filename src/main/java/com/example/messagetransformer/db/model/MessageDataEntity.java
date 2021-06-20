package com.example.messagetransformer.db.model;

import com.example.messagetransformer.models.MessageData;
import com.example.messagetransformer.models.MessageDataType;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "messageData")
public class MessageDataEntity {
    private String id;
    @Indexed
    private String originalValue;
    private String transformedValue;
    private MessageDataType messageDataType;
    private Long counter;
    private LocalDateTime created;
    private LocalDateTime modified;

    public static MessageDataEntity convert(final MessageData messageData) {
        final MessageDataEntity entity = new MessageDataEntity();
        entity.setMessageDataType(messageData.getMessageDataType());
        entity.setTransformedValue(messageData.getConvertedString());
        entity.setOriginalValue(messageData.getOriginalString());
        entity.setCreated(LocalDateTime.now());
        entity.setModified(entity.getModified());
        entity.setCounter(1L);
        return entity;
    }

    @Override
    public String toString() {
        return String.format(
                "MessageData[id=%s, revertedValue='%s', messageDataType='%s', counter='%s']",
                id, transformedValue, messageDataType, counter);
    }
}
