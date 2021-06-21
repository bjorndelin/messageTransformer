package com.example.messagetransformer.services;

import com.example.messagetransformer.converters.MessageDataConverter;
import com.example.messagetransformer.db.model.MessageDataEntity;
import com.example.messagetransformer.db.repository.MessageDataRepository;
import com.example.messagetransformer.exceptions.MessageTransformerException;
import com.example.messagetransformer.models.MessageData;
import com.example.messagetransformer.models.MessageDataType;
import com.example.messagetransformer.models.requests.MessageTransformerRequest;
import com.example.messagetransformer.transformers.MessageTransformerInterface;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MessageTransformerServiceTest {
    @Test
    void test_transformer() throws MessageTransformerException {
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

        messageTransformerService.transformMessage(request);
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
