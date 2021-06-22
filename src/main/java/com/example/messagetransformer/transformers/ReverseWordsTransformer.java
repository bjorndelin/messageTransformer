package com.example.messagetransformer.transformers;

import com.example.messagetransformer.exceptions.ErrorType;
import com.example.messagetransformer.exceptions.MessageTransformerException;
import com.example.messagetransformer.models.MessageData;
import com.example.messagetransformer.models.MessageDataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class ReverseWordsTransformer implements MessageTransformerInterface {

    private static final Logger logger = LoggerFactory.getLogger(ReverseWordsTransformer.class);
    private static final char[] DIVIDER_CHARS = " ,.!?:;".toCharArray();

    private static final HashSet<Character> DIVIDERS = new HashSet<>(IntStream
            .range(0, DIVIDER_CHARS.length)
            .mapToObj(i -> DIVIDER_CHARS[i])
            .collect(Collectors.toList()));

    @Override
    public MessageData transform(final MessageData message) throws MessageTransformerException {
        validate(message);
        return transformMessageData(message);
    }

    private MessageData transformMessageData(final MessageData message) {
        final StringBuilder builder = new StringBuilder();
        final String check = message.getOriginalString();

        int i = 0;

        while (i < check.length()) {
            int index = findDividerIndex(check , i);
            if (index == -1) {
                index = check.length();
            }

            final String word = check.substring(i, index);
            final String flipped = flipWord(word);

            builder.append(flipped);
            if (index < check.length()) {
                builder.append(check.substring(index, index + 1));
            }

            i = index + 1;
        }

        message.setConvertedString(builder.toString());

        return message;
    }

    private int findDividerIndex(final String check, int start) {
        for (int i = start; i < check.length(); i++) {
            if (DIVIDERS.contains(check.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    private String flipWord(final String word) {
        final StringBuilder builder = new StringBuilder();
        for (char c : word.toCharArray()) {
            builder.insert(0, c);
        }

        return builder.toString();
    }

    private void validate(final MessageData message) throws MessageTransformerException {
        if (message.getOriginalString() == null) {
            logger.warn("Message not set! Validation failed!");
            throw new MessageTransformerException("Message not set!", ErrorType.BAD_FORMAT);
        }

        message.setOriginalString(message.getOriginalString().trim());

        if (message.getOriginalString().isEmpty()) {
            logger.warn("Message empty! Validation failed!");
            throw new MessageTransformerException("Message empty!", ErrorType.BAD_FORMAT);
        }
    }

    @Override
    public MessageDataType getMessageDataType() {
        return MessageDataType.WORD_REVERSAL;
    }
}
