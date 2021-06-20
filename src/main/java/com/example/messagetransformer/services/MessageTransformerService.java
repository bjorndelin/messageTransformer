package com.example.messagetransformer.services;

import com.example.messagetransformer.db.model.MessageDataEntity;
import com.example.messagetransformer.db.repository.MessageDataRepository;
import com.example.messagetransformer.exceptions.ErrorType;
import com.example.messagetransformer.exceptions.MessageTransformerException;
import com.example.messagetransformer.models.MessageData;
import com.example.messagetransformer.models.MessageDataType;
import com.example.messagetransformer.models.requests.MessageTransformerRequest;
import com.example.messagetransformer.models.responses.MessageTransformerResponse;
import com.example.messagetransformer.transformers.MessageTransformerInterface;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Optional;

@Service
public class MessageTransformerService {

    private MessageDataRepository repository;
    private EnumMap<MessageDataType, MessageTransformerInterface> messageTransformerMap;

    public MessageTransformerService(
            final MessageDataRepository repository,
            final List<MessageTransformerInterface> messageTransformers) {
        this.repository = repository;
        messageTransformerMap = new EnumMap<>(MessageDataType.class);
        messageTransformers.forEach(message -> messageTransformerMap.put(message.getMessageDataType(), message));
    }

    public MessageTransformerResponse transformMessage(final MessageTransformerRequest request) throws MessageTransformerException {
        final MessageData messageData = MessageData.convert(request);
        final MessageData transformed = transform(messageData);
        saveData(transformed);

        return MessageTransformerResponse.convert(transformed);
    }

    private MessageData transform(final MessageData messageData) throws MessageTransformerException{
        final MessageTransformerInterface messageTransformer =
                messageTransformerMap.get(messageData.getMessageDataType());

        if (messageTransformer == null) {
            throw new MessageTransformerException("Transformer not found", ErrorType.NOT_FOUND);
        }

        return messageTransformer.transform(messageData);
    }

    private void saveData(final MessageData messageData) {
        final Optional<MessageDataEntity> optionalEntity = repository.findByMessageDataTypeAndOriginalValue(
                messageData.getMessageDataType(), messageData.getOriginalString());

        if (optionalEntity.isPresent()) {
            final MessageDataEntity entity = optionalEntity.get();
            entity.setModified(LocalDateTime.now());
            entity.setCounter(entity.getCounter() + 1);
            repository.save(entity);
        } else {
            final MessageDataEntity entity = MessageDataEntity.convert(messageData);
            repository.save(entity);
        }
    }
}
