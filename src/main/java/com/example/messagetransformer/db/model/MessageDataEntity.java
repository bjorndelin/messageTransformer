package com.example.messagetransformer.db.model;

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

    @Override
    public String toString() {
        return String.format(
                "MessageData[id=%s, revertedValue='%s', messageDataType='%s', counter='%s']",
                id, transformedValue, messageDataType, counter);
    }
}
