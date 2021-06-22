package com.example.messagetransformer.transformers;

import com.example.messagetransformer.exceptions.MessageTransformerException;
import com.example.messagetransformer.models.MessageData;

import com.example.messagetransformer.models.MessageDataType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ReverseWordsTransformerTests {

    @Test
    void test_transform_successful() throws MessageTransformerException {
        final String originalMessage = "The red fox crosses the ice, intent on none of my business.";
        final String expectedResult = "ehT der xof sessorc eht eci, tnetni no enon fo ym ssenisub.";
        testReversal(originalMessage, expectedResult);
    }

    @Test
    void test_transform_successful2() throws MessageTransformerException {
        final String originalMessage = ",.!?:; are all dividers and will not be flipped";
        final String expectedResult  = ",.!?:; era lla sredivid dna lliw ton eb deppilf";
        testReversal(originalMessage, expectedResult);
    }

    @Test
    void test_transform_successful3() throws MessageTransformerException {
        final String originalMessage = "     Spaces before and after the sentence will be omitted     ";
        final String expectedResult  = "secapS erofeb dna retfa eht ecnetnes lliw eb dettimo";
        testReversal(originalMessage, expectedResult);
    }

    private void testReversal(final String originalMessage, final String expectedResult) throws MessageTransformerException {
        final ReverseWordsTransformer transformer = new ReverseWordsTransformer();
        final MessageData data = new MessageData();

        data.setOriginalString(originalMessage);
        data.setMessageDataType(MessageDataType.WORD_REVERSAL);

        final MessageData result = transformer.transform(data);

        Assertions.assertEquals(expectedResult, result.getConvertedString());
    }

    @Test
    void test_transform_invalid_orignial_message() throws MessageTransformerException {
        final ReverseWordsTransformer transformer = new ReverseWordsTransformer();

        final String originalMessage = "";

        final MessageData data = new MessageData();
        data.setOriginalString(originalMessage);
        data.setMessageDataType(MessageDataType.WORD_REVERSAL);

        try {
            transformer.transform(data);
            Assertions.fail();
        } catch (MessageTransformerException e) {
            Assertions.assertEquals("Message empty!", e.getMessage());
        }
    }

    @Test
    void test_transform_invalid_orignial_message_blank_space() throws MessageTransformerException {
        final ReverseWordsTransformer transformer = new ReverseWordsTransformer();

        final String originalMessage = null;

        final MessageData data = new MessageData();
        data.setOriginalString(originalMessage);
        data.setMessageDataType(MessageDataType.WORD_REVERSAL);

        try {
            transformer.transform(data);
            Assertions.fail();
        } catch (MessageTransformerException e) {
            Assertions.assertEquals("Message not set!", e.getMessage());
        }
    }
}
