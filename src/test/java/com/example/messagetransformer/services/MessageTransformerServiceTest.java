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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MessageTransformerServiceTest {
    @Test
    void test_transformer_successful() throws MessageTransformerException {
        final MessageDataRepository repository = mock(MessageDataRepository.class);
        final MessageTransformerInterface messageTransformerInterface = mock(MessageTransformerInterface.class);
        final List<MessageTransformerInterface> messageTransformers = new ArrayList<>();
        messageTransformers.add(messageTransformerInterface);
        final MessageTransformerRequest request = getMessageTransformerRequest();
        final MessageDataEntity entity = getMessageDataEntity(request);
        final MessageData result = getMessageData(request, "sihT si a tset. tI sah emos tnereffid sredivid ni ti! yaS tahW!?");

        when(messageTransformerInterface.getMessageDataType()).thenReturn(MessageDataType.WORD_REVERSAL);
        when(repository.findByMessageDataTypeAndOriginalValue(any(), any())).thenReturn(Optional.of(entity));
        when(messageTransformerInterface.transform(any())).thenReturn(result);

        final MessageTransformerService messageTransformerService = new MessageTransformerService(repository, messageTransformers);

        final MessageTransformerResponse response = messageTransformerService.transformMessage(request);

        Assertions.assertEquals(response.getResult(), result.getConvertedString());
    }

    @Test
    void test_transformer_invalid_message() {
        final MessageDataRepository repository = mock(MessageDataRepository.class);
        final MessageTransformerInterface messageTransformerInterface = mock(MessageTransformerInterface.class);
        final List<MessageTransformerInterface> messageTransformers = new ArrayList<>();
        messageTransformers.add(messageTransformerInterface);

        final MessageTransformerRequest request = getMessageTransformerRequest();
        request.setMessage("");

        when(messageTransformerInterface.getMessageDataType()).thenReturn(MessageDataType.WORD_REVERSAL);

        final MessageTransformerService messageTransformerService = new MessageTransformerService(repository, messageTransformers);

        try {
            messageTransformerService.transformMessage(request);
            Assertions.fail();
        } catch (MessageTransformerException e) {
            Assertions.assertEquals("Bad request", e.getMessage());
            Assertions.assertEquals(ErrorType.BAD_FORMAT, e.getErrorType());
        }
    }

    @Test
    void test_transformer_invalid_message_data_type() {
        final MessageDataRepository repository = mock(MessageDataRepository.class);
        final MessageTransformerInterface messageTransformerInterface = mock(MessageTransformerInterface.class);
        final List<MessageTransformerInterface> messageTransformers = new ArrayList<>();
        messageTransformers.add(messageTransformerInterface);

        final MessageTransformerRequest request = getMessageTransformerRequest();
        request.setMessageDataType("INVALID_TYPE");

        when(messageTransformerInterface.getMessageDataType()).thenReturn(MessageDataType.WORD_REVERSAL);

        final MessageTransformerService messageTransformerService = new MessageTransformerService(repository, messageTransformers);

        try {
            messageTransformerService.transformMessage(request);
            Assertions.fail();
        } catch (MessageTransformerException e) {
            Assertions.assertEquals("Bad request", e.getMessage());
            Assertions.assertEquals(ErrorType.BAD_FORMAT, e.getErrorType());
        }
    }

    @Test
    void test_transformer_invalid_transformer() {
        final MessageDataRepository repository = mock(MessageDataRepository.class);
        final List<MessageTransformerInterface> messageTransformers = new ArrayList<>();

        final MessageTransformerRequest request = getMessageTransformerRequest();

        final MessageTransformerService messageTransformerService = new MessageTransformerService(repository, messageTransformers);

        try {
            messageTransformerService.transformMessage(request);
            Assertions.fail();
        } catch (MessageTransformerException e) {
            Assertions.assertEquals("Transformer not found", e.getMessage());
            Assertions.assertEquals(ErrorType.NOT_FOUND, e.getErrorType());
        }
    }

    private MessageTransformerRequest getMessageTransformerRequest() {
        final MessageTransformerRequest request = new MessageTransformerRequest();
        request.setMessage("This is a test. It has some different dividers in it! Say What!?");
        request.setMessageDataType(MessageDataType.WORD_REVERSAL.name());
        return request;
    }

    private MessageDataEntity getMessageDataEntity(final MessageTransformerRequest request) throws MessageTransformerException {
        final MessageData data = MessageDataConverter.convertToMessageData(request);
        final MessageDataEntity entity = MessageDataConverter.convertToMessageDataEntity(data);
        return entity;
    }

    private MessageData getMessageData(final MessageTransformerRequest request, final String result) throws MessageTransformerException {
        final MessageData data = MessageDataConverter.convertToMessageData(request);
        data.setConvertedString(result);
        return data;
    }
}
