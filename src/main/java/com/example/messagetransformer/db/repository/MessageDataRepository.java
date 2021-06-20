package com.example.messagetransformer.db.repository;

import com.example.messagetransformer.db.model.MessageDataEntity;
import com.example.messagetransformer.models.MessageDataType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MessageDataRepository extends MongoRepository<MessageDataEntity, String> {

    Optional<MessageDataEntity> findByMessageDataTypeAndOriginalValue(
            final MessageDataType messageDataType, final String transformedValue);

}
