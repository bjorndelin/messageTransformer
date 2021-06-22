package com.example.messagetransformer.controllers;

import com.example.messagetransformer.exceptions.ErrorType;
import com.example.messagetransformer.exceptions.MessageTransformerException;
import com.example.messagetransformer.models.MessageDataType;
import com.example.messagetransformer.models.requests.MessageTransformerRequest;
import com.example.messagetransformer.models.responses.ErrorMessageResponse;
import com.example.messagetransformer.models.responses.MessageTransformerResponse;
import com.example.messagetransformer.services.MessageTransformerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MessageTransformerControllerTest {

    @Test
    void test_transform_message() throws MessageTransformerException {
        final MessageTransformerService messageTransformerService = mock(MessageTransformerService.class);
        final MessageTransformerController messageTransformerController =
                new MessageTransformerController(messageTransformerService);
        final MessageTransformerRequest messageTransformerRequest = getMessageTransformerRequest();
        final MessageTransformerResponse messageTransformerResponse = getMessageTransformerResponse();

        when(messageTransformerService.transformMessage(any())).thenReturn(messageTransformerResponse);

        final ResponseEntity<MessageTransformerResponse> response =
                messageTransformerController.transformMessage(messageTransformerRequest);

        Assertions.assertEquals(response.getBody().getResult(), messageTransformerResponse.getResult());
    }

    @Test
    void test_transform_message_throw_exception() throws MessageTransformerException {
        final MessageTransformerService messageTransformerService = mock(MessageTransformerService.class);
        final MessageTransformerController messageTransformerController =
                new MessageTransformerController(messageTransformerService);
        final MessageTransformerRequest messageTransformerRequest = getMessageTransformerRequest();

        when(messageTransformerService.transformMessage(any()))
                .thenThrow(new MessageTransformerException("message", ErrorType.BAD_FORMAT));

        try {
            final ResponseEntity<MessageTransformerResponse> response =
                    messageTransformerController.transformMessage(messageTransformerRequest);
            Assertions.fail();
        } catch (MessageTransformerException e) {
            Assertions.assertEquals("message", e.getMessage());
            Assertions.assertEquals(ErrorType.BAD_FORMAT, e.getErrorType());
        }
    }

    @Test
    void handle_message_transformer_exception() {
        final MessageTransformerService messageTransformerService = mock(MessageTransformerService.class);
        final MessageTransformerController messageTransformerController = new MessageTransformerController(messageTransformerService);
        final MessageTransformerException exception = new MessageTransformerException("This is a message", ErrorType.BAD_FORMAT);
        final ResponseEntity<ErrorMessageResponse> entity = messageTransformerController.handleException(exception);

        Assertions.assertEquals("This is a message", entity.getBody().getError());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, entity.getStatusCode());
    }

    @Test
    void handle_method_argument_not_valid_exception() {
        final MessageTransformerService messageTransformerService = mock(MessageTransformerService.class);
        final MessageTransformerController messageTransformerController = new MessageTransformerController(messageTransformerService);
        final MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        final ResponseEntity<ErrorMessageResponse> entity = messageTransformerController.handleException(exception);

        Assertions.assertEquals("Bad request!", entity.getBody().getError());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST,entity.getStatusCode());
    }

    private MessageTransformerRequest getMessageTransformerRequest() {
        final MessageTransformerRequest messageTransformerRequest = new MessageTransformerRequest();
        messageTransformerRequest.setMessageDataType(MessageDataType.WORD_REVERSAL.name());
        messageTransformerRequest.setMessage("This is a message.");

        return messageTransformerRequest;
    }

    private MessageTransformerResponse getMessageTransformerResponse() {
        final MessageTransformerResponse messageTransformerResponse = new MessageTransformerResponse();
        messageTransformerResponse.setResult("sihT si a egassem.");

        return messageTransformerResponse;
    }
}
