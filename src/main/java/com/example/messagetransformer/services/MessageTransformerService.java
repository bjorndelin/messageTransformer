package com.example.messagetransformer.services;

import com.example.messagetransformer.converters.MessageDataConverter;
import com.example.messagetransformer.db.model.MessageDataEntity;
import com.example.messagetransformer.db.repository.MessageDataRepository;
import com.example.messagetransformer.exceptions.ErrorType;
import com.example.messagetransformer.exceptions.MessageTransformerException;
import com.example.messagetransformer.models.MessageData;
import com.example.messagetransformer.models.MessageDataType;
import com.example.messagetransformer.models.requests.MessageTransformerRequest;
import com.example.messagetransformer.models.responses.MessageTransformerResponse;
import com.example.messagetransformer.transformers.MessageTransformerInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Optional;

@Service
public class MessageTransformerService {

    Logger logger = LoggerFactory.getLogger(MessageTransformerService.class);

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
        logger.debug("Transforming message for messageDataType: {}", request.getMessageDataType());

        final MessageData messageData = MessageDataConverter.convertToMessageData(request);
        final MessageData transformed = transform(messageData);
        saveData(transformed);

        return MessageDataConverter.convertToMessageTransformerResponse(transformed);
    }

    private MessageData transform(final MessageData messageData) throws MessageTransformerException{
        final MessageTransformerInterface messageTransformer =
                messageTransformerMap.get(messageData.getMessageDataType());

        if (messageTransformer == null) {
            logger.error("Couldn't create a MessageTransformer from messageDataType: {}",
                    messageData.getMessageDataType());
            throw new MessageTransformerException("Transformer not found", ErrorType.NOT_FOUND);
        }

        return messageTransformer.transform(messageData);
    }

    private void saveData(final MessageData messageData) {
        final Optional<MessageDataEntity> optionalEntity = repository.findByMessageDataTypeAndOriginalValue(
                messageData.getMessageDataType(), messageData.getOriginalString());

        if (optionalEntity.isPresent()) {
            logger.debug("Update and save message to db...");

            final MessageDataEntity entity = optionalEntity.get();
            entity.setModified(LocalDateTime.now());
            entity.setCounter(entity.getCounter() + 1);
            repository.save(entity);
        } else {
            logger.debug("Save new message to db...");
            final MessageDataEntity entity = MessageDataConverter.convertToMessageDataEntity(messageData);
            repository.save(entity);
        }
    }
}
