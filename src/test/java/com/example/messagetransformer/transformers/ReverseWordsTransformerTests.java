package com.example.messagetransformer.transformers;

import com.example.messagetransformer.exceptions.MessageTransformerException;
import com.example.messagetransformer.models.MessageData;

import com.example.messagetransformer.models.MessageDataType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ReverseWordsTransformerTests {
    @Test
    void test_transform() throws MessageTransformerException {
        final ReverseWordsTransformer transformer = new ReverseWordsTransformer();

        final String originalMessage = "The red fox crosses the ice, intent on none of my business.";
        final String expectedResult = "ehT der xof sessorc eht eci, tnetni no enon fo ym ssenisub.";

        final MessageData data = new MessageData();
        data.setOriginalString(originalMessage);
        data.setMessageDataType(MessageDataType.WORD_REVERSAL);

        final MessageData result = transformer.transform(data);

        Assertions.assertEquals(result.getConvertedString(), expectedResult);
    }
}
