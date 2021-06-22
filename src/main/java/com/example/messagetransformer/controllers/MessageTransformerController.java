package com.example.messagetransformer.controllers;

import com.example.messagetransformer.exceptions.MessageTransformerException;
import com.example.messagetransformer.models.MessageDataType;
import com.example.messagetransformer.models.requests.MessageTransformerRequest;
import com.example.messagetransformer.models.responses.ErrorMessageResponse;
import com.example.messagetransformer.models.responses.MessageDataTypeResponse;
import com.example.messagetransformer.models.responses.MessageTransformerResponse;
import com.example.messagetransformer.services.MessageTransformerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController("/")
@CrossOrigin(origins = "*", methods = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST})
public class MessageTransformerController {

    private final MessageTransformerService messageTransformerService;

    public MessageTransformerController(final MessageTransformerService messageTransformerService) {
        this.messageTransformerService = messageTransformerService;
    }

    @PostMapping("transform")
    public ResponseEntity<MessageTransformerResponse> transformMessage(@Valid @RequestBody final MessageTransformerRequest request)
            throws MessageTransformerException {
        final MessageTransformerResponse response = messageTransformerService.transformMessage(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("transform")
    public ResponseEntity<MessageDataTypeResponse> transformMessage() {
        final List<String> list = new ArrayList<>();
        final MessageDataTypeResponse response = new MessageDataTypeResponse();
        response.setMessageDataTypes(list);

        Arrays.stream(MessageDataType.values()).forEach(m -> {
            if (m != MessageDataType.UNKNOWN) {
                list.add(m.name());
            }
        });

        return ResponseEntity.ok(response);
    }

    @ExceptionHandler({ MessageTransformerException.class })
    public ResponseEntity<ErrorMessageResponse> handleException(MessageTransformerException e) {
        final ErrorMessageResponse error = new ErrorMessageResponse(e.getMessage());
        if (e.getErrorType() != null) {
            return new ResponseEntity<>(error, e.getErrorType().getHttpStatus());
        } else {
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public ResponseEntity<ErrorMessageResponse> handleException(MethodArgumentNotValidException e) {
        if (e.getFieldError() == null) {
            final ErrorMessageResponse error = new ErrorMessageResponse("Bad request!");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        } else {
            final FieldError fieldError = e.getFieldError();
            final String message;
            if (fieldError == null) {
                message = "Bad Request!";
            } else {
                message = fieldError.getDefaultMessage();
            }
            final ErrorMessageResponse error = new ErrorMessageResponse(message);
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }
}
